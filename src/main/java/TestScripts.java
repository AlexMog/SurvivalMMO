import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;
import org.luaj.vm2.lib.jse.JsePlatform;

import survivalgame.server.world.ecs.components.ScriptableComponent;

class EntityTemplate {
    private float mXSize, mYSize, mZSize;
    private final Map<String, LuaFunction> mEvents = new HashMap<>();
    private final Map<Integer, RpcFunction> mRpcs = new HashMap<>();
    private int mRpcCurrentId = 0;
    
    public EntityTemplate(String name, int clientId) {
        System.out.println("Entity template: " + name + " clientId: " + clientId);
    }
    
    public void setCollisionsBox(float xSize, float ySize, float zSize) {
        mXSize = xSize;
        mYSize = ySize;
        mZSize = zSize;
    }
    
    public void registerEvent(String name, LuaFunction function) {
        mEvents.put(name, function);
    }
    
    public void registerRpc(LuaFunction function, String[] types) {
        mRpcs.put(mRpcCurrentId++, new RpcFunction(function, types));
    }
    
    public float getXSize() {
        return mXSize;
    }

    public void setXSize(float xSize) {
        this.mXSize = xSize;
    }

    public float getYSize() {
        return mYSize;
    }

    public void setYSize(float ySize) {
        this.mYSize = ySize;
    }

    public float getZSize() {
        return mZSize;
    }

    public void setZSize(float zSize) {
        this.mZSize = zSize;
    }

    private static class RpcFunction {
        private final List<Type> mTypes = new ArrayList<>();
        private final LuaFunction mFunction;
        
        public void execute(Object methodParent, List<Object> arguments) {
            LuaValue[] params = new LuaValue[arguments.size()];
            
            for (int i = 0; i < params.length; ++i) params[i] = CoerceJavaToLua.coerce(arguments.get(i));
            
            mFunction.invoke(params);
        }
        
        public RpcFunction(LuaFunction function, String[] types) {
            mFunction = function;
            for (String type : types) {
                switch (type.toLowerCase()) {
                case "integer":
                    mTypes.add(int.class);
                    break;
                case "boolean":
                    mTypes.add(boolean.class);
                    break;
                case "byte":
                    mTypes.add(byte.class);
                    break;
                case "short":
                    mTypes.add(short.class);
                    break;
                case "long":
                    mTypes.add(long.class);
                    break;
                case "float":
                    mTypes.add(float.class);
                    break;
                case "double":
                    mTypes.add(double.class);
                    break;
                case "char":
                    mTypes.add(char.class);
                    break;
                }
            }
        }
    }
}

class CreateEntityTemplate extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue name, LuaValue clientId) {
        return CoerceJavaToLua.coerce(new EntityTemplate((String) CoerceLuaToJava.coerce(name, String.class),
                (int) CoerceLuaToJava.coerce(clientId, Integer.class)));
    }
    
}

class Invoke extends ThreeArgFunction {

    @Override
    public LuaValue call(LuaValue templateId, LuaValue xPos, LuaValue yPos) {
        // TODO Add invoke systems
        return null;
    }
    
}

public class TestScripts {
    public static void main(String[] args) {
        Globals globals = JsePlatform.debugGlobals();
        globals.set("Core", new LuaTable());
        globals.get("Core").set("createEntityTemplate", new CreateEntityTemplate());
        globals.get("Core").set("invoke", new Invoke());
        globals.get("Core").set("destroy", new CreateEntityTemplate());
        
        ScriptableComponent cmp = new ScriptableComponent();
        
        File scripts = new File("scripts/");
        
        List<File> files = getFilesRec(scripts);
        for (File f : files) {
            if (f.getName().equals("autorun.lua")) globals.loadfile(f.getAbsolutePath()).call();
        }
//        cmp.initMethods();
        
//        cmp.awake();
        
//        cmp.setEnabled(true);
        
//        cmp.executeMethod(Events.CollisionEnter);
    }
    
    private static List<File> getFilesRec(File root) {
        List<File> files = new ArrayList<>();
        File[] fileList = root.listFiles();
        for (File f : fileList) {
            if (f.isDirectory()) files.addAll(getFilesRec(f));
            else files.add(f);
        }
        return files;
    }
}
