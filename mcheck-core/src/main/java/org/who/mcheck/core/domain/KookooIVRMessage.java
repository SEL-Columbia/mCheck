package org.who.mcheck.core.domain;

import org.motechproject.ivr.domain.IVRMessage;
import org.springframework.stereotype.Component;

@Component
public class KookooIVRMessage implements IVRMessage {
    @Override
    public String getText(String key) {
        return null;
    }

    @Override
    public String getWav(String key, String preferredLangCode) {
        return null;
    }
}
