package gameplay;

import java.io.File;

public class Initializer {
    XMLParser myXMLParser;

    public Initializer(File file){
        myXMLParser = new XMLParser();
        initGameData(file);
    }

    private void initGameData(File file){
        myXMLParser.loadFile(file);
        GameData.setGameData(myXMLParser.getPlayers(), myXMLParser.getEntities(), myXMLParser.getTiles(),
                myXMLParser.getPhases(), myXMLParser.getNodes(), myXMLParser.getEdges(), myXMLParser.getTurn());
    }

    public void startGame(){
        myXMLParser.getTurn().startPhase();
    }

    public void saveGame(){
        String xmlString = GameData.saveGameData();
    }

}