package org.who.formhub.listener;

import org.who.formhub.listener.event.FakeCheckMotherRequest;

public class FakeMCheckController {
    public void registerMother(FakeCheckMotherRequest request) {
    }

    public void methodWhichThrowsAnException(FakeCheckMotherRequest request) {
        throw new RuntimeException("Boo");
    }
}
