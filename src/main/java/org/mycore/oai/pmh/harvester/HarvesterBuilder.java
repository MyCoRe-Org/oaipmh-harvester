package org.mycore.oai.pmh.harvester;

import java.lang.reflect.Constructor;

import org.mycore.oai.pmh.FriendsDescription;
import org.mycore.oai.pmh.OAIIdentifierDescription;
import org.mycore.oai.pmh.harvester.jaxb.JAXBHarvester;

/**
 * Use this builder to construct a {@link Harvester} instance. To use default config call {@link #createNewInstance(String)}, you can also create your own
 * config and call {@link #createNewInstance(String, HarvesterConfig)}.
 * 
 * @author Matthias Eichner
 */
public abstract class HarvesterBuilder {

    private static Class<? extends Harvester> harvesterClass;

    private static HarvesterConfig defaultConfig;

    static {
        harvesterClass = JAXBHarvester.class;
        defaultConfig = new HarvesterConfig();
        defaultConfig.registerDescription("oai-identifier", OAIIdentifierDescription.class);
        defaultConfig.registerDescription("friends", FriendsDescription.class);
    }

    /**
     * Creates a new {@link Harvester} instance. The default config is used.
     * 
     * @param baseURL
     *            base URL of OAI-PMH data provider (e.g. http://archive.thulb.uni-jena.de/hisbest/oai2). Sample OAI-PMH data providers can be found <a
     *            href="http://www.openarchives.org/Register/BrowseSites">here</a>.
     * @return new instance of <code>Harvester</code>
     */
    public static Harvester createNewInstance(String baseURL) {
        return createNewInstance(baseURL, defaultConfig);
    }

    /**
     * Creates a new {@link Harvester} instance.
     * 
     * @param baseURL
     *            base URL of OAI-PMH data provider (e.g. http://archive.thulb.uni-jena.de/hisbest/oai2). Sample OAI-PMH data providers can be found <a
     *            href="http://www.openarchives.org/Register/BrowseSites">here</a>.
     * @param config
     *            use special configuration
     * @return new instance of <code>Harvester</code>
     */
    public static Harvester createNewInstance(String baseURL, HarvesterConfig config) {
        try {
            Constructor<? extends Harvester> constructor = harvesterClass.getConstructor(String.class, HarvesterConfig.class);
            return constructor.newInstance(baseURL, config);
        } catch (Exception exc) {
            throw new RuntimeException("while creating harvester", exc);
        }
    }

    /**
     * Set your own harvester implementation.
     * 
     * @param harvesterClass
     */
    public static void setHarvesterClass(Class<? extends Harvester> harvesterClass) {
        HarvesterBuilder.harvesterClass = harvesterClass;
    }

    public static Class<? extends Harvester> getHarvesterClass() {
        return harvesterClass;
    }

    /**
     * Returns the default config.
     * 
     * @return instance of <code>HarvesterConfig</code>
     */
    public static HarvesterConfig getDefaultConfig() {
        return defaultConfig;
    }

}
