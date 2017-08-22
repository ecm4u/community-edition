package org.alfresco.service.cmr.repository;

/**
 * Holder for the {@code ecm4u.mltext.disable} property. See
 * https://github.com/ecm4u/community-edition/issues/2
 *
 * @author Lutz Horn lutz.horn@ecm4u.de
 */
public class MLTextSwitch {

    private boolean on;

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

}
