package gogame;

public enum BoardSpace{
    EMPTY,
    BLACK,
    WHITE;

    public static BoardSpace fromStone(Stone stone){
        switch(stone){
            case Stone.BLACK -> {
                return BoardSpace.BLACK;
            }
            case Stone.WHITE -> {
                return BoardSpace.WHITE;
            }
            default -> {
                return BoardSpace.EMPTY;
            }
        }
    }
}
