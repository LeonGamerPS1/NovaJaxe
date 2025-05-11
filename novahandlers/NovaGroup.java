package NovaJaxe.novahandlers;

import NovaJaxe.Main;

import java.util.Vector;

public class NovaGroup extends Main {

    public Vector<NovaAnimSprite> members = new Vector<NovaAnimSprite>(0);

    public void addToGroup(NovaAnimSprite sprite) {
        members.add(sprite);
    }
    public void destroy() {
        members = new Vector<NovaAnimSprite>(0);
    }
    public NovaGroup() {

    }
}
