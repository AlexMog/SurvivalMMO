import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import survivalgame.server.world.ecs.components.ScriptableComponent;
import survivalgame.server.world.ecs.components.ScriptableComponent.Events;

public class TestScripts {
    public static void main(String[] args) {
        Globals globals = JsePlatform.debugGlobals();
        ScriptableComponent cmp = new ScriptableComponent();
        
        File scripts = new File("scripts/");
        
        List<File> files = getFilesRec(scripts);
        for (File f : files) {
            System.out.println(f.getAbsolutePath());
            if (f.getName().equals("autorun.lua")) {
                globals.loadfile(f.getAbsolutePath()).call();
            }
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
