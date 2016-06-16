<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" encoding="iso-8859-1"/>

    <xsl:strip-space elements="*"/>

    <xsl:template match="/*/child::*">
        <xsl:for-each select="child::*">
            <xsl:if test="position() != last()">
                <xsl:text>&apos;</xsl:text><xsl:value-of select="normalize-space(.)"/><xsl:text>&apos;,</xsl:text>
            </xsl:if>
            <xsl:if test="position()  = last()">
                <xsl:text>&apos;</xsl:text><xsl:value-of select="normalize-space(.)"/><xsl:text>&apos;</xsl:text><xsl:text>&#xA;&#xD;</xsl:text>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>

