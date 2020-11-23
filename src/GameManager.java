/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Abhinav
 */
public class GameManager {
    Player p;
    Game g;
    GameManager(String playerName,int n,int target,String s){
        p=new Player(playerName);
        g=Game.fromGameMode(n,target,s);
    }
}
