package gogame;

import static java.util.Arrays.stream;

public enum BoardSize {
    NINE (9),
    THIRTEEN (13),
    NINETEEN (19);

    private final int size;

    BoardSize(int size){
        this.size = size;
    }

    public int getSize(){
        return this.size;
    }

    @Override
    public String toString(){
        return this.size + "x" + this.size;
    }

    public static BoardSize fromString(String s){
        return stream(values())
                .filter(boardSize -> boardSize.toString().equals(s))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("NOT A VALID ARGUMENT"));
    }

    public static String[] getStringValues(){
        return stream(values())
                .map(BoardSize::toString)
                .toArray(String[]::new);
    }
}
