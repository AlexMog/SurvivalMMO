package survivalgame.config;

public class Config extends JsonConfiguration {

    public Server server = new Server();
    public HubServer hubServer = new HubServer();
    public Game game;
    public World world = new World();
    public Class[] classes = {new Class()};
    public Effect[] effects = {new Effect()};
    public EntitySkin[] entitySkins = {new EntitySkin()};
    
    public static class Game {
        public byte teams;
        public String type;
        public boolean needLogin = false;
        public String gameMode = "";
    }
    
    public static class EntitySkin {
        public int skinId;
        public String skinUrl = "";
    }
    
    public static class HubServer {
        public String host;
        public int port;
        public String pass;
    }

    public static class Server {
        public int port = 4243;
        public int maxConnections = 100;
    }
    
    public static class Class {
//        public int id;
        public String name = "", description = "", spriteUrl = "", bulletClazz = "";
        public float baseHp, baseDamages, baseSpeed,
            baseHpRegen, baseBulletSpeed, baseReloadSpeed,
            hpPerLevel, damagesPerLevel, speedPerLevel, hpRegenPerLevel,
            bulletSpeedPerLevel, reloadSpeedPerLevel, baseWidth, baseHeight,
            widthPerLevel, heightPerLevel, baseBulletSize, bulletSizePerLevel,
            baseBulletTicLife, bulletTicLifePerLevel;
        public Spell[] spells = {new Spell()};
        public Weapon weapon = new Weapon();
        
        public static class Spell extends WithVars {
            public String name = "", description = "", iconUrl = "", clazzName = "";
            public int level, range;
            public int cooldown, castTime;
            public int[] effects = {0};
        }
        
        public static class Weapon {
            public String name = "", description = "", spriteUrl = "", clazz = "";
        }
    }
    
    private static class WithVars {
        protected Var[] vars = {new Var()};
        
        public Var.VarValue getVar(String name) {
            for (Var v : vars) if (v.name.equalsIgnoreCase(name)) return v.value;
            throw new RuntimeException("Var not found: " + name);
        }
    }
    
    public static class Effect extends WithVars {
        public int id, maxStacks;
        public String name = "", description = "", iconUrl = "", drawUrl = "";
        public boolean stackable, infinite;
        public String clazzName = "";
    }
    
    public static class Var {
        public String name = "";
        public VarValue value;
        
        public static class VarValue {
            public float floatValue = 1f;
            public String stringValue = "";
        }
    }

    public static class World {

        public View view = new View();
        public Border border = new Border();
        public Food food = new Food();

        public static class View {

            public double baseX = 1024;
            public double baseY = 592;
        }

        public static class Border {

            public double left = 0;
            public double right = 6000;
            public double top = 0;
            public double bottom = 6000;
        }

        public static class Food {

            public int spawnInterval = 20; // In ticks
            public int spawnPerInterval = 10; // How many food to spawn per interval
            public int startAmount = 100; // The amount of food to start the world with
            public int maxAmount = 500; // The maximum amount of food in the world at once
        }
    }
    
    public Config.Effect getEffectFromId(int id) {
        for (Config.Effect e : effects) if (e.id == id) return e;
        return null;
    }
}
