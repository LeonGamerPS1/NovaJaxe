package NovaJaxe.novahandlers;

import NovaJaxe.Main;

import java.util.Vector;

public class NovaSpriteGroup extends Main {

    public Vector<NovaSprite> members = new Vector<NovaSprite>(0);

    public void addToGroup(NovaSprite sprite) {
        members.add(sprite);
    }
    public void destroy() {
        members = new Vector<NovaSprite>(0);
    }
    public NovaSpriteGroup() {

    }
}
