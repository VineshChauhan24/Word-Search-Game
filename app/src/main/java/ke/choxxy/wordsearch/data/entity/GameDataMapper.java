package ke.choxxy.wordsearch.data.entity;


import ke.choxxy.wordsearch.commons.Mapper;
import ke.choxxy.wordsearch.commons.generator.StringGridGenerator;
import ke.choxxy.wordsearch.model.GameData;
import ke.choxxy.wordsearch.model.Grid;

/**
 * Created by abdularis on 08/07/17.
 */

public class GameDataMapper extends Mapper<GameDataEntity, GameData> {
    @Override
    public GameData map(GameDataEntity obj) {
        if (obj == null) return null;

        Grid grid = new Grid(obj.getGridRowCount(), obj.getGridColCount());
        GameData gr = new GameData();
        gr.setId(obj.getId());
        gr.setName(obj.getName());
        gr.setDuration(obj.getDuration());
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
        ent.setId(obj.getId());
        ent.setName(obj.getName());
        ent.setDuration(obj.getDuration());

        if (obj.getGrid() != null) {
            ent.setGridRowCount(obj.getGrid().getRowCount());
            ent.setGridColCount(obj.getGrid().getColCount());
            ent.setGridData(obj.getGrid().toString());
        }

        ent.setUsedWords(obj.getUsedWords());

        return ent;
    }
}
