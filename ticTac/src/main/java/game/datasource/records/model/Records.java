package game.datasource.records.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "records")
public class Records {
    @Id
    @Column(unique = true, nullable = false)
    private String id;

    @Column(nullable = false)
    private int wins;

    @Column(nullable = false)
    private int loses;

    @Column(nullable = false)
    private int draw;

    @Column(nullable = false)
    private double percentWin;

    public Records() {
        this.id = UUID.randomUUID().toString();
        this.wins = 0;
        this.loses = 0;
        this.draw = 0;
        this.percentWin = 0.0;
    }

    public Records(String id) {
        this.id = id;
        this.wins = 0;
        this.loses = 0;
        this.draw = 0;
        this.percentWin = 0.0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
        recalcPercentWin();
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
        recalcPercentWin();
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public double getPercentWin() {
        return percentWin;
    }

    private void recalcPercentWin() {
        int total = wins + loses;
        this.percentWin = (total > 0) ? (double) wins * 100 / total : 0.0;
    }
}
