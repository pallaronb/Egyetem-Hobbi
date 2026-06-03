package gogame;

public enum Stone {
    BLACK,
    WHITE;

    public Stone opposite(){
        if(this == BLACK){
            return WHITE;
        }
        else{
            return BLACK;
        }
    }
}
