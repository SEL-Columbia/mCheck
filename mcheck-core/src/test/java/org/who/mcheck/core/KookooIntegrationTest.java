package org.who.mcheck.core;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.who.formhub.api.util.EasyMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-mcheck.xml")
@Ignore
public class KookooIntegrationTest {

    @Autowired
    @Qualifier("ivrServiceKookoo")
    private IVRService service;

    @Ignore
    public void shouldCallSpecifiedNumber() throws Exception {
        CallRequest callRequest = new CallRequest("988062181", EasyMap.mapOf("", ""),
                "http://li310-155.members.linode.com:8080/mcheckivr/kookoo/ivr?tree=mCheckTree&trP=Lw&ln=en");

        service.initiateCall(callRequest);
    }
}

