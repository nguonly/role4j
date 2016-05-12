package net.role4j.bytebuddy;

import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.type.TypeDescription;

/**
 * Created by nguonly on 4/30/16.
 */
public class ProxyNamingStrategy implements NamingStrategy {
    @Override
    public String subclass(TypeDescription.Generic generic) {
        return null;
    }

    @Override
    public String redefine(TypeDescription typeDescription) {
        return null;
    }

    @Override
    public String rebase(TypeDescription typeDescription) {
        return null;
    }
}
