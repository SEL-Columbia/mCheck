package org.who.formhub.listener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.scheduler.domain.MotechEvent;
import org.who.formhub.api.contract.FormHubFormInstance;
import org.who.formhub.api.util.EasyMap;
import org.who.formhub.listener.event.FakeCheckMotherRequest;
import org.who.formhub.listener.event.FormHubFormEvent;

import static com.ibm.icu.impl.Assert.fail;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.who.formhub.api.util.EasyMap.mapOf;

public class FormHubFormSubmissionRouterTest {
    @Mock
    private FakeMCheckController mCheckController;

    private FormHubFormSubmissionRouter router;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        router = new FormHubFormSubmissionRouter();
    }

    @Test
    public void shouldDispatchToMethodWithSameNameAsHandler() throws Exception {
        router.registerForDispatch(mCheckController);
        FormHubFormInstance formInstance = new FormHubFormInstanceBuilder().withName("checkMother").withHandler("registerMother").withMapping("mother_", "motherName").withContent(mapOf("mother_", "Theresa")).build();

        router.handle(eventFor(formInstance));

        verify(mCheckController).registerMother(new FakeCheckMotherRequest("Theresa"));
    }

    @Test
    public void shouldNotDoAnythingIfNoMethodIsFound() throws Exception {
        router.registerForDispatch(mCheckController);
        FormHubFormInstance formInstance = new FormHubFormInstanceBuilder().withName("checkMother").withHandler("invalidHandler").withMapping("mother_", "motherName").withContent(mapOf("mother_", "Theresa")).build();

        router.handle(eventFor(formInstance));

        verifyZeroInteractions(mCheckController);
    }

    @Test
    public void shouldNotDoAnythingIfNoneIsRegistered() throws Exception {
        router.registerForDispatch(null);
        FormHubFormInstance formInstance = new FormHubFormInstanceBuilder().withName("invalidCheckMother").withHandler("registerMother").withMapping("mother_", "motherName").withContent(mapOf("mother_", "Theresa")).build();

        router.handle(eventFor(formInstance));
    }

    @Test(expected = FormDispatchFailedException.class)
    public void shouldThrowExceptionIfDispatchFails() throws Exception {
        router.registerForDispatch(mCheckController);
        mCheckController.methodWhichThrowsAnException(any(FakeCheckMotherRequest.class));
        doThrow(new RuntimeException("boo")).when(mCheckController).methodWhichThrowsAnException(any(FakeCheckMotherRequest.class));
        FormHubFormInstance formInstance = new FormHubFormInstanceBuilder().withName("checkMother").withHandler("methodWhichThrowsAnException").withMapping("mother_", "motherName").withContent(mapOf("mother_", "Theresa")).build();

        router.handle(eventFor(formInstance));
    }

    @Test
    public void shouldDispatchToOtherMethodsEvenIfOneDispatchFails() throws Exception {
        router.registerForDispatch(mCheckController);

        mCheckController.methodWhichThrowsAnException(any(FakeCheckMotherRequest.class));
        doThrow(new RuntimeException("boo")).when(mCheckController).methodWhichThrowsAnException(any(FakeCheckMotherRequest.class));

        MotechEvent event = eventFor(
                new FormHubFormInstanceBuilder().withName("checkMother").withHandler("registerMother").withMapping("mother_", "motherName").withContent(mapOf("mother_", "Theresa")).build(),
                new FormHubFormInstanceBuilder().withName("checkMother").withHandler("methodWhichThrowsAnException").withMapping("mother_", "motherName").withContent(mapOf("mother_", "Theresa")).build(),
                new FormHubFormInstanceBuilder().withName("checkMother").withHandler("methodWhichThrowsAnException").withMapping("mother_", "motherName").withMapping("ph_no", "number").withContent(EasyMap.create("mother_", "Theresa").put("ph_no", "9876543210").map()).build(),
                new FormHubFormInstanceBuilder().withName("checkMother").withHandler("registerMother").withMapping("mother_", "motherName").withContent(mapOf("mother_", "Theresa")).build());

        try {
            router.handle(event);
            fail("Should have thrown an exception");
        } catch (FormDispatchFailedException e) {
            assertThat(e.innerExceptions().size(), is(2));

            assertTrue(e.innerExceptions().get(0).getMessage().startsWith("Failed during dispatch. Method: methodWhichThrowsAnException, Parameter JSON: "));
            assertTrue(e.innerExceptions().get(0).getMessage().endsWith("{\"motherName\":\"Theresa\"}"));

            assertThat(e.innerExceptions().get(0).getCause().getMessage(), is("boo"));

            assertTrue(e.innerExceptions().get(1).getMessage().startsWith("Failed during dispatch. Method: methodWhichThrowsAnException, Parameter JSON: "));
            assertTrue(e.innerExceptions().get(1).getMessage().endsWith("{\"motherName\":\"Theresa\",\"number\":\"9876543210\"}") || e.innerExceptions().get(1).getMessage().endsWith("{\"number\":\"9876543210\",\"motherName\":\"Theresa\"}"));

            assertThat(e.innerExceptions().get(1).getCause().getMessage(), is("boo"));
        }

        verify(mCheckController, times(2)).registerMother(new FakeCheckMotherRequest("Theresa"));
    }

    private MotechEvent eventFor(FormHubFormInstance... formHubFormInstances) {
        return new FormHubFormEvent(asList(formHubFormInstances)).toMotechEvent();
    }
}
