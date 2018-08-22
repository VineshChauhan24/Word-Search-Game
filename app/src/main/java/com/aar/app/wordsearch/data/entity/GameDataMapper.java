package com.aar.app.wordsearch.data.entity;


import com.aar.app.wordsearch.commons.Mapper;
import com.aar.app.wordsearch.commons.generator.StringGridGenerator;
import com.aar.app.wordsearch.data.entity.GameDataEntity;
import com.aar.app.wordsearch.model.GameData;
import com.aar.app.wordsearch.model.Grid;

/**
 * Created by abdularis on 08/07/17.
 */

public class GameDataMapper extends Mapper<GameDataEntity, GameData> {
    @Override
    public GameData map(GameDataEntity obj) {
        if (obj == null) return null;

        GameData gr = new GameData();
        gr.setInfo(obj.getInfo());
        Grid grid = new Grid(obj.getGridRowCount(), obj.getGridColCount());
        gr.setGrid(grid);

        if (obj.getGridData() != null && obj.getGridData().length() > 0) {
            new StringGridGenerator().setGrid(obj.getGridData(), grid.getArray());
        }

        gr.addUsedWords(obj.getUsedWords());

        return gr;
    }

    @Override
    public GameDataEntity revMap(GameData obj) {
        if (obj == null) return null;

        GameDataEntity ent = new GameDataEntity();
        ent.setInfo(obj.getInfo());

        if (obj.getGrid() != null) {
            ent.setGridRowCount(obj.getGrid().getRowCount());
            ent.setGridColCount(obj.getGrid().getColCount());
            ent.setGridData(obj.getGrid().toString());
        }

        ent.setUsedWords(obj.getUsedWords());

        return ent;
    }
}
