/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hslf.usermodel;

import static org.apache.poi.hslf.usermodel.HSLFTextParagraph.getPropVal;
import static org.apache.poi.hslf.usermodel.HSLFTextParagraph.setPropVal;

import java.awt.Color;

import org.apache.poi.hslf.model.textproperties.*;
import org.apache.poi.hslf.model.textproperties.TextPropCollection.TextPropType;
import org.apache.poi.sl.usermodel.TextRun;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;


/**
 * Represents a run of text, all with the same style
 *
 */
public final class HSLFTextRun implements TextRun {
	protected POILogger logger = POILogFactory.getLogger(this.getClass());

	/** The TextRun we belong to */
	private HSLFTextParagraph parentParagraph;
	private String _runText = "";
	private String _fontFamily;
	
	/**
	 * Our paragraph and character style.
	 * Note - we may share these styles with other RichTextRuns
	 */
	private TextPropCollection characterStyle = new TextPropCollection(1, TextPropType.character);

	/**
	 * Create a new wrapper around a rich text string
	 * @param parent The parent paragraph
	 */
	public HSLFTextRun(HSLFTextParagraph parentParagraph) {
		this.parentParagraph = parentParagraph;
	}
	
	public TextPropCollection getCharacterStyle() {
	    return characterStyle;
	}

	public void setCharacterStyle(TextPropCollection characterStyle) {
	    assert(characterStyle != null);
	    this.characterStyle = characterStyle;
	}
	
	/**
	 * Supply the SlideShow we belong to
	 */
	public void updateSheet() {
		if (_fontFamily != null) {
			setFontFamily(_fontFamily);
			_fontFamily = null;
		}
	}

	/**
	 * Get the length of the text
	 */
	public int getLength() {
		return _runText.length();
	}

	/**
	 * Fetch the text, in raw storage form
	 */
	public String getRawText() {
		return _runText;
	}

	/**
	 * Change the text
	 */
	public void setText(String text) {
	    _runText = HSLFTextParagraph.toInternalString(text);
	}

	// --------------- Internal helpers on rich text properties -------

	/**
	 * Fetch the value of the given flag in the CharFlagsTextProp.
	 * Returns false if the CharFlagsTextProp isn't present, since the
	 *  text property won't be set if there's no CharFlagsTextProp.
	 */
	private boolean isCharFlagsTextPropVal(int index) {
		return getFlag(index);
	}

	protected boolean getFlag(int index) {
	    if (characterStyle == null) return false;

		BitMaskTextProp prop = (BitMaskTextProp)characterStyle.findByName(CharFlagsTextProp.NAME);

		if (prop == null){
            int txtype = parentParagraph.getRunType();
			HSLFSheet sheet = parentParagraph.getSheet();
			if (sheet != null) {
				HSLFMasterSheet master = sheet.getMasterSheet();
				if (master != null){
					prop = (BitMaskTextProp)master.getStyleAttribute(txtype, parentParagraph.getIndentLevel(), CharFlagsTextProp.NAME, true);
				}
			} else {
				logger.log(POILogger.WARN, "MasterSheet is not available");
			}
		}

		return prop == null ? false : prop.getSubValue(index);
	}

	/**
	 * Set the value of the given flag in the CharFlagsTextProp, adding
	 *  it if required.
	 */
	private void setCharFlagsTextPropVal(int index, boolean value) {
	    // TODO: check if paragraph/chars can be handled the same ...
		if (getFlag(index) != value) setFlag(index, value);
	}

	/**
	 * Sets the value of the given Paragraph TextProp, add if required
	 * @param propName The name of the Paragraph TextProp
	 * @param val The value to set for the TextProp
	 */
	public void setCharTextPropVal(String propName, Integer val) {
	    setPropVal(characterStyle, propName, val);
	}


	// --------------- Friendly getters / setters on rich text properties -------

	/**
	 * Is the text bold?
	 */
	public boolean isBold() {
		return isCharFlagsTextPropVal(CharFlagsTextProp.BOLD_IDX);
	}

	/**
	 * Is the text bold?
	 */
	public void setBold(boolean bold) {
		setCharFlagsTextPropVal(CharFlagsTextProp.BOLD_IDX, bold);
	}

	/**
	 * Is the text italic?
	 */
	public boolean isItalic() {
		return isCharFlagsTextPropVal(CharFlagsTextProp.ITALIC_IDX);
	}

	/**
	 * Is the text italic?
	 */
	public void setItalic(boolean italic) {
		setCharFlagsTextPropVal(CharFlagsTextProp.ITALIC_IDX, italic);
	}

	/**
	 * Is the text underlined?
	 */
	public boolean isUnderlined() {
		return isCharFlagsTextPropVal(CharFlagsTextProp.UNDERLINE_IDX);
	}

	/**
	 * Is the text underlined?
	 */
	public void setUnderlined(boolean underlined) {
		setCharFlagsTextPropVal(CharFlagsTextProp.UNDERLINE_IDX, underlined);
	}

	/**
	 * Does the text have a shadow?
	 */
	public boolean isShadowed() {
		return isCharFlagsTextPropVal(CharFlagsTextProp.SHADOW_IDX);
	}

	/**
	 * Does the text have a shadow?
	 */
	public void setShadowed(boolean flag) {
		setCharFlagsTextPropVal(CharFlagsTextProp.SHADOW_IDX, flag);
	}

	/**
	 * Is this text embossed?
	 */
	 public boolean isEmbossed() {
		return isCharFlagsTextPropVal(CharFlagsTextProp.RELIEF_IDX);
	}

	/**
	 * Is this text embossed?
	 */
	 public void setEmbossed(boolean flag) {
		setCharFlagsTextPropVal(CharFlagsTextProp.RELIEF_IDX, flag);
	}

	/**
	 * Gets the strikethrough flag
	 */
	public boolean isStrikethrough() {
		return isCharFlagsTextPropVal(CharFlagsTextProp.STRIKETHROUGH_IDX);
	}

	/**
	 * Sets the strikethrough flag
	 */
	public void setStrikethrough(boolean flag) {
		setCharFlagsTextPropVal(CharFlagsTextProp.STRIKETHROUGH_IDX, flag);
	}

	/**
	 * Gets the subscript/superscript option
	 *
	 * @return the percentage of the font size. If the value is positive, it is superscript, otherwise it is subscript
	 */
	public int getSuperscript() {
		TextProp tp = getPropVal(characterStyle, "superscript", parentParagraph);
		return tp == null ? 0 : tp.getValue();
	}

	/**
	 * Sets the subscript/superscript option
	 *
	 * @param val the percentage of the font size. If the value is positive, it is superscript, otherwise it is subscript
	 */
	public void setSuperscript(int val) {
	    setPropVal(characterStyle, "superscript", val);
	}

    @Override
	public Double getFontSize() {
        TextProp tp = getPropVal(characterStyle, "font.size", parentParagraph);
        return tp == null ? null : (double)tp.getValue();
	}


	@Override
	public void setFontSize(Double fontSize) {
	    Integer iFontSize = (fontSize == null) ? null : fontSize.intValue();
		setCharTextPropVal("font.size", iFontSize);
	}

	/**
	 * Gets the font index
	 */
	public int getFontIndex() {
        TextProp tp = getPropVal(characterStyle, "font.index", parentParagraph);
        return tp == null ? -1 : tp.getValue();
	}

	/**
	 * Sets the font index
	 */
	public void setFontIndex(int idx) {
		setCharTextPropVal("font.index", idx);
	}


	/**
	 * Sets the font name to use
	 */
	public void setFontFamily(String fontFamily) {
	    HSLFSheet sheet = parentParagraph.getSheet();
	    HSLFSlideShow slideShow = (sheet == null) ? null : sheet.getSlideShow();
		if (sheet == null || slideShow == null) {
			//we can't set font since slideshow is not assigned yet
			_fontFamily = fontFamily;
			return;
		}
		// Get the index for this font (adding if needed)
		int fontIdx = slideShow.getFontCollection().addFont(fontFamily);
		setCharTextPropVal("font.index", fontIdx);
	}

	/**
	 * Gets the font name
	 */
	@Override
	public String getFontFamily() {
        HSLFSheet sheet = parentParagraph.getSheet();
        HSLFSlideShow slideShow = (sheet == null) ? null : sheet.getSlideShow();
		if (sheet == null || slideShow == null) {
			return _fontFamily;
		}
        TextProp tp = getPropVal(characterStyle, "font.index", parentParagraph);
        if (tp == null) { return null; }
		return slideShow.getFontCollection().getFontWithId(tp.getValue());
	}

	/**
	 * @return font color as RGB value
	 * @see java.awt.Color
	 */
	public Color getFontColor() {
		TextProp tp = getPropVal(characterStyle, "font.color", parentParagraph);
		return (tp == null) ? null
	        : HSLFTextParagraph.getColorFromColorIndexStruct(tp.getValue(), parentParagraph.getSheet());
	}

	/**
	 * Sets color of the text, as a int bgr.
	 * (PowerPoint stores as BlueGreenRed, not the more
	 *  usual RedGreenBlue)
	 * @see java.awt.Color
	 */
	public void setFontColor(int bgr) {
		setCharTextPropVal("font.color", bgr);
	}

	/**
	 * Sets color of the text, as a java.awt.Color
	 */
	public void setFontColor(Color color) {
		// In PowerPont RGB bytes are swapped, as BGR
		int rgb = new Color(color.getBlue(), color.getGreen(), color.getRed(), 254).getRGB();
		setFontColor(rgb);
	}

    protected void setFlag(int index, boolean value) {
        BitMaskTextProp prop = (BitMaskTextProp)characterStyle.addWithName(CharFlagsTextProp.NAME);
        prop.setSubValue(value, index);
    }

    public HSLFTextParagraph getTextParagraph() {
        return parentParagraph;
    }
    
    public TextCap getTextCap() {
        return TextCap.NONE;
    }

    public boolean isSubscript() {
        return false;
    }

    public boolean isSuperscript() {
        return false;
    }

    public byte getPitchAndFamily() {
        return 0;
    }
}
