package com.aar.app.wordsearch.data.xml;

import android.content.Context;
import android.content.res.AssetManager;

import com.aar.app.wordsearch.domain.data.source.WordDataSource;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.IOException;

import javax.inject.Inject;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by abdularis on 31/07/17.
 */

public class WordXmlDataSource implements WordDataSource {

    private static final String ASSET_WORD_BANK_FILE = "words.xml";

    private AssetManager mAssetManager;

    @Inject
    public WordXmlDataSource(Context context) {
        mAssetManager = context.getAssets();
    }

    @Override
    public void getWords(Callback callback) {
        try {
            XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            SaxWordBankHandler wordBankHandler = new SaxWordBankHandler();
            reader.setContentHandler(wordBankHandler);
            reader.parse(getXmlInputSource());

            // return result
            callback.onWordsLoaded(wordBankHandler.getWords());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private InputSource getXmlInputSource() throws IOException {
        return new InputSource(mAssetManager.open(ASSET_WORD_BANK_FILE));
    }
}
