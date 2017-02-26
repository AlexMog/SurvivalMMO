package survivalgame.server.world.ecs.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class ScriptableComponent extends EnablableComponent {
    private Map<String, LuaValue> mScripts = new HashMap<>();
    private List<List<LuaFunction>> mEventFunctions = new ArrayList<List<LuaFunction>>(Events.values().length);
    private boolean mStarted = false;
    
    public Map<String, LuaValue> getScripts() {
        return mScripts;
    }
    
    public LuaValue getScript(String scriptIdentifier) {
        return mScripts.get(scriptIdentifier);
    }
    
    /**
     * Attach a new script to this entity
     * @param scriptIdentifier The script identifier (used by the "getScript" method)
     * @param script The script
     */
    public void addScript(String scriptIdentifier, LuaValue script) {
        mScripts.put(scriptIdentifier, script);
    }
    
    private void setEventFunction(int ordinal, LuaFunction fnc) {
        int scripts = mScripts.size();
        List<LuaFunction> functions = new ArrayList<>(scripts);
        for (int i = 0; i < scripts; ++i) functions.set(i, fnc);
        mEventFunctions.set(ordinal, functions);
    }
    
    // TODO Use a template system instead, to cache Lua scripts instead of defining each time the functions.
    public void initMethods() {
        for (LuaValue script : mScripts.values()) {
//            script.set("ENT", CoerceJavaToLua.coerce(mEntity));
            for (Events e : Events.values()) {
                try {
                    System.out.println(e.getEventFunction());
                    LuaValue fnc = script.get(e.getEventFunction());
                    if (fnc == LuaValue.NIL) continue;
                    setEventFunction(e.ordinal(), (LuaFunction) fnc);
                } catch (LuaError ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (mEnabled != enabled) {
            if (enabled) {
                executeMethod(Events.Enable);
                if (!mStarted) {
                    executeMethod(Events.Start);
                    mStarted = true;
                }
            } else executeMethod(Events.Disable);
        }
        super.setEnabled(enabled);
    }
    
    public void executeMethod(Events e, Object...args) {
        List<LuaFunction> list = mEventFunctions.get(e.ordinal());
        if (list == null || list.size() == 0) return;
        for (LuaFunction method : list) {
            LuaValue[] vars = new LuaValue[args.length];
            for (int i = 0; i < args.length; ++i) vars[i] = CoerceJavaToLua.coerce(args[i]);
            if (method != null) method.invoke(LuaValue.varargsOf(vars));
        }
    }
    
    public void awake() {
        executeMethod(Events.Awake);
    }
    
    public enum Events {
        /**
         * Called when a ScriptableEntity is created
         * TODO Implement 
         */
        Awake("awake"),
        /**
         * Called at each update
         * Implemented in ScriptSystem
         */
        Update("update"),
        /**
         * Called when an entity enter the collision boundaries of this entity
         * Implemented in ScriptableSystem
         */
        CollisionEnter("onCollisionEnter"), 
        /**
         * Called when an entity exits the collision boundaries of this entity
         * Implemented in ScriptableSystem
         */
        CollisionExit("onCollisionExit"),
        /**
         * Called when an entity is currently in the boundaries of this entity
         * (This is includes the OnCollisionEnter entities)
         * Implemented in ScriptableSystem
         */
        CollisionStay("onCollisionStay"),
        /**
         * Called when the entity is enabled
         * Implemented in ScriptableComponent
         */
        Enable("onEnable"),
        /**
         * Called when the entity is disabled
         * Implemented in ScriptableComponent
         */
        Disable("onDisable"),
        /**
         * Called when the entity is destroyed
         * TODO Implement
         */
        Destroy("onDestroy"),
        /**
         * Called when the entity is enabled for the first time
         * Implemented in ScriptableComponent
         */
        Start("start")
        ;
        
        private final String mEventFunction;
        
        Events(String eventFunction) {
            mEventFunction = eventFunction;
        }
        
        public String getEventFunction() {
            return mEventFunction;
        }
    }
}
