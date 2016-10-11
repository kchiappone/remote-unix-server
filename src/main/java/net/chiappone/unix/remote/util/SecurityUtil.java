package net.chiappone.unix.remote.util;

import net.chiappone.util.security.algorithms.Algorithm;
import net.chiappone.util.security.hashers.StringHasher;

/**
 * Security utility class to help with verifying keys and generating hashes.
 *
 * @author Kurtis Chiappone
 */
public class SecurityUtil {

    /**
     * Verifies the given request key.
     *
     * @param secret  secret key
     * @param address Client IP address
     * @param key     secret key
     * @return true if verified
     */
    public static boolean verifyKey( String secret, String address, String key ) {

        return generateHash( secret, address ).matches( key );

    }

    /**
     * Generate a hash consisting of the IP address and secret key.
     *
     * @param secret  secret key
     * @param address Client IP address
     * @return hash string
     */
    public static String generateHash( String secret, String address ) {

        return new StringHasher().hash( Algorithm.SHA256, address + "_" + secret );

    }

}
