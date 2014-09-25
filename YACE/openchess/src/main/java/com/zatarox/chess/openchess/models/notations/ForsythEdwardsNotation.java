package com.zatarox.chess.openchess.models.notations;

import com.zatarox.chess.openchess.models.materials.ChessBoard;
import com.zatarox.chess.openchess.models.materials.Piece;
import com.zatarox.chess.openchess.models.materials.Trait;

public final class ForsythEdwardsNotation {

    public static final String START_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private ForsythEdwardsNotation() {
    }

    public static final char getNotation(Trait trait, Piece piece) {
        char result;
        switch (piece) {
            case PAWN:
                result = 'P';
                break;
            case KNIGHT:
                result = 'N';
                break;
            case BISHOP:
                result = 'B';
                break;
            case QUEEN:
                result = 'Q';
                break;
            case ROOK:
                result = 'R';
                break;
            case KING:
                result = 'K';
                break;
            default:
                result = '?';
        }
        if (trait == Trait.BLACK) {
            result = Character.toLowerCase(result);
        }
        return result;
    }

    public static ChessBoard create(String notation) {
        throw new UnsupportedOperationException();
    }

    public static String toString(ChessBoard pos) {
        throw new UnsupportedOperationException();
    }
}
