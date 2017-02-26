package survivalgame.server;

import java.io.File;
import java.util.logging.Logger;

import survivalgame.config.Config;
import survivalgame.config.JsonConfiguration;
import survivalgame.server.network.NetworkManager;
import survivalgame.server.world.WorldManager;

public class Server {
    public static final Logger log = Logger.getGlobal();
    private Config mConfig = new Config();
    private File mConfigFile = new File("configs.json");
    private NetworkManager mNetworkManager = new NetworkManager(this);
    private WorldManager mWorldManager = new WorldManager();

    public static void main(String[] args) throws Exception {
        new Server().start();
    }
    
    private Server() {}
    
    public void start() throws Exception {
        if (!mConfigFile.exists()) {
            mConfigFile.createNewFile();
            mConfig.save(mConfigFile);
        }
        mConfig = JsonConfiguration.load(mConfigFile, Config.class);
        mWorldManager.init();
        mWorldManager.start();
        mNetworkManager.start();
    }
    
    public Config getConfig() {
        return mConfig;
    }
    
    public NetworkManager getNetworkManager() {
        return mNetworkManager;
    }

}
