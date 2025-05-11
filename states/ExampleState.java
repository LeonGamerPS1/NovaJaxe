package NovaJaxe.states;

import NovaJaxe.backend.utils.ConsoleColors;
import NovaJaxe.backend.utils.CoolUtil;
import NovaJaxe.backend.utils.WindowUtil;
import NovaJaxe.novahandlers.*;

public class ExampleState extends NovaState {

    public void preUpdate() {
        super.preUpdate();
    }

    public void update() {
        super.update();
    }

    public void postUpdate() {
        super.postUpdate();
    }

    public void beat() {
        super.beat();
    }

    public void step() {
        super.step();
    }

    public void create() {
        super.create();

        NovaSprite exampleSprite = new NovaSprite("imagePath in images/", x, y);
        add(exampleSprite);
    }

    public void postCreate() {
        super.postCreate();
    }

    public void destroy() {
        super.destroy();
    }
}
