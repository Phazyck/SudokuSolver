package sudoku;

public class CharSet {

    public static void main(String[] args) {
        CharSet a = new CharSet("the quick brown fox");
        System.out.println(a.size() + " : " + a);

        CharSet b = new CharSet("jumps over the lazy dog");
        System.out.println(b.size() + " : " + b);

        CharSet u = union(a,b);
        System.out.println(u.size() + " : " + u);
        
        CharSet i = intersection(a,b);
        System.out.println(i.size() + " : " + i);
        
        CharSet c = complement(a,b);
        System.out.println(c.size() + " : " + c);
        
        CharSet vertical = new CharSet("631");
        CharSet horizontal = new CharSet("842763");
        CharSet block = new CharSet("6584");
        CharSet used = CharSet.union(CharSet.union(horizontal, vertical), block);
        CharSet free = CharSet.complement(new CharSet("123456789"), used);
        System.out.println(used);
        System.out.println(free);
    }
    private final CharNode root = new CharNode();

    /**
     * Construct a set of unique characters from a string.
     *
     * @param chars The string containing the characters.
     */
    public CharSet(String chars) {
        this(chars.toCharArray());
    }

    /**
     * Construct a set of unique characters from an array of chars.
     *
     * @param chars The array containing the characters.
     */
    public CharSet(char[] chars) {
        for (char c : chars) {
            root.add(c);
        }
    }

    /**
     * Constructs an empty CharSet.
     */
    public CharSet() {
    }

    /**
     * Makes a new set containing the unique characters of both a and b.
     *
     * @param a The first CharSet.
     * @param b The second CharSet.
     * @return The union of a and b.
     */
    public static CharSet union(CharSet a, CharSet b) {
        return new CharSet(a.toString() + b.toString());
    }

    /**
     * Makes a new set containing the characters which exist in both a and b.
     *
     * @param a The first CharSet.
     * @param b The second CharSet.
     * @return The intersection of a and b.
     */
    public static CharSet intersection(CharSet a, CharSet b) {
        return complement(a, complement(a, b));
    }

    /**
     * Makes a new set containing the characters which exist in a, but not in b.
     *
     * @param a The first CharSet.
     * @param b The second CharSet.
     * @return The relative complement of B in A.
     */
    public static CharSet complement(CharSet a, CharSet b) {
        CharSet complement = new CharSet(a.toString());
        for (char c : b.toString().toCharArray()) {
            complement.remove(c);
        }
        return complement;
    }

    /**
     * Adds a character to the CharSet.
     *
     * @param c
     */
    public void add(char c) {
        root.add(c);
    }

    /**
     * Removes a character from the CharSet.
     *
     * @param c The character to be removed.
     */
    public void remove(char c) {
        root.remove(c);
    }

    /**
     * NOTE: Returns Character.MAXVALUE if empty.
     *
     * @return the first (smallest) char in the set.
     */
    public char peek() {
        return root.item;
    }

    /**
     * @return the number of unique chars contained in the set.
     */
    public int size() {
        return root.size();
    }

    @Override
    public String toString() {
        return root.toString();
    }

    private class CharNode {

        private char item;
        private CharNode next;

        public CharNode() {
            item = Character.MAX_VALUE;
            next = null;
        }

        public char getChar() {
            return item;
        }

        public CharNode getNext() {
            return next;
        }

        public void add(char c) {
            if (c < item) {
                CharNode node = clone();
                this.item = c;
                this.next = node;
            } else if (c > item) {
                next.add(c);
            }
        }

        public void remove(char c) {
            if (c > item && next != null) {
                next.remove(c);
            } else if (c == item) {
                this.item = next.item;
                this.next = next.next;
            }
        }

        @Override
        public String toString() {
            if (next == null) {
                return "";
            } else {
                return item + next.toString();
            }
        }

        @Override
        public CharNode clone() {
            CharNode clone = new CharNode();
            clone.item = this.item;
            clone.next = this.next;
            return clone;
        }

        public int size() {
            if (this.next == null) {
                return 0;
            } else {
                return 1 + next.size();
            }
        }
    }
}
