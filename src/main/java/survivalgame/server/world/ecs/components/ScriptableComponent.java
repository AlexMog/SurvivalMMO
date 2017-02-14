package survivalgame.server.world.ecs.components;

import org.luaj.vm2.LuaValue;

import com.artemis.Component;

public class ScriptableComponent extends Component {
    private LuaValue mScript;
    
    public LuaValue getScript() {
        return mScript;
    }
    
    public void setScript(LuaValue script) {
        mScript = script;
    }
}
