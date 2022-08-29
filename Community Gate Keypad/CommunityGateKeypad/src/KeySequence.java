import java.util.LinkedList;

/**
 * Author: Robin Acosta 3/8/2021
 * A Key Sequence is a utility object which stores a sequence of Keys.
 * It provides several utility functions.
 */

public class KeySequence {
    private LinkedList<Key> keys = new LinkedList<>();

    /**
     * Constructs and returns an empty KeySequence.
     * @return ks, an empty key sequence
     */
    public KeySequence KeySequence(){
        KeySequence ks = new KeySequence();
        return ks;
    }

    /**
     * Takes a KeySequence and returns a copy.
     * @param keySequence
     * @return ks, a copy of the given key sequence
     */
    public KeySequence KeySequence(KeySequence keySequence){
        KeySequence ks = keySequence;
        return ks;
    }

    /**
     * Tests if the KeySequence it is called on and the provided KeySequence are equal.
     * @param keySequence
     * @return true if the two KeySequences are equal, false if not
     */
    public boolean equal(KeySequence keySequence){
        if(keys.size()!=keySequence.keys.size()){
            return false;
        }
        for(int i = 0; i<keys.size();i++){
            if(!keys.get(i).equals(keySequence.keys.get(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * Appends the provided Key to the KeySequence.
     * @param key to be added at the end of the KeySequence
     * @return number of keys in the KeySequence
     */
    public int add(Key key){
        if (keys.size()<10) {
            keys.add(key);
        }
        return keys.size();
    }

    /**
     * Removes the Key at position n in the KeySequence.
     * @param n , the position of the key  to be removed
     * @return number of keys in the KeySequence
     */
    public int remove(int n){
        if(n>=0 && n<keys.size()){
            keys.remove(n);
        }
        return keys.size();
    }

    /**
     * Gets the Key at position n in the KeySequence it is called on.
     * @param n , the position of the key  to be returned
     * @return key at the nth position
     */
    public Key get(int n){
        return keys.get(n);
    }

    /**
     * Trims the KeySequence down to 4 keys
     * @return KeySequence containing 4 keys
     */
    public KeySequence trim(){
        KeySequence keySequence = KeySequence();
        LinkedList<Key> fourKeys = new LinkedList<>();
        fourKeys.add(keys.get(0));
        fourKeys.add(keys.get(1));
        fourKeys.add(keys.get(2));
        fourKeys.add(keys.get(3));
        keySequence.keys = fourKeys;
        return keySequence;
    }

    /**
     * Translates the KeySequence into an integer.
     * @return  integer translation of the KeySequence if all keys are numbers,
     *          -1 if KeySequence contains non-number Keys.
     */
    public int toNum(){
        int n = 0;
        for(Key key: keys){
            if (key.isNum()){
                n*=10;
                n+= key.toNum();
            }
            else {
                return -1;
            }
        }
        return n;
    }

    /**
     * Tests if the KeySequence is comprised of four number keys.
     * @return true if KeySequence is comprised of four number keys, false if not
     */
    public boolean validate(){
        if(toNum()!=-1 && keys.size()==4) {
            return true;
        }
        return false;
    }

    /**
     * Returns the number of keys.
     * @return integer representation of the number of keys
     */
    public int length() {
        return keys.size();
    }
}
