<?xml version="1.0" encoding="UTF-8" ?> 
  <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:variable name="para-font-header-size">10pt</xsl:variable>
	<xsl:variable name="para-font-title-size">14pt</xsl:variable>
	<xsl:variable name="para-font-size">8pt</xsl:variable>
	<xsl:variable name="para-font-small-size">6pt</xsl:variable>
	<xsl:template match="/">
		 <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fox="http://xml.apache.org/fop/extensions">
		 	<fo:layout-master-set>
		 		<fo:simple-page-master master-name="all" page-height="29.7cm" page-width="21cm" margin-top="0.5cm" margin-bottom="0.5cm" margin-left="1cm" margin-right="1cm">
		  			<fo:region-body margin-top="2cm" margin-bottom="2cm" /> 
		  			<fo:region-before extent="3cm" /> 
		  			<fo:region-after extent="0.5cm" /> 
		  		</fo:simple-page-master>
		  	</fo:layout-master-set>
			<xsl:for-each select="doc/etudiant">
			 	<fo:page-sequence id="{generate-id(.)}" master-reference="all" initial-page-number="1" force-page-count="no-force" font-family="Helvetica, Arial, sans">
			 		<fo:static-content flow-name="xsl-region-before">
			  			<xsl:call-template name="before" /> 
			  		</fo:static-content>
					<fo:static-content flow-name="xsl-region-after">
						<fo:block font-size="{$para-font-size}" text-align="center">
							<fo:table table-layout="fixed" width="100%" >
				  				<fo:table-column column-width="proportional-column-width(0.5)" /> 
				  				<fo:table-column column-width="proportional-column-width(1)" /> 
				  				<fo:table-column column-width="proportional-column-width(0.5)" /> 
				 				<fo:table-body>
				  					<fo:table-row  vertical-align="middle">
				  						<fo:table-cell>
					  						<fo:block font-size="{$para-font-small-size}" text-align="left">
					  							Edité le : 
				  								<xsl:value-of select="dateedit"/>
				  							</fo:block>
				  						</fo:table-cell>
							  			<fo:table-cell>
							 				<fo:block font-size="{$para-font-size}" text-align="center">
									  						Page <fo:page-number/>
												/ <fo:page-number-citation-last ref-id="{generate-id(.)}"/>
							  				</fo:block>
							  			</fo:table-cell>
				  						<fo:table-cell>
				  							<fo:block font-size="{$para-font-small-size}" text-align="right">
				  								<xsl:value-of select="codeetape"/>
				  							</fo:block>
				  						</fo:table-cell>
							  		</fo:table-row>
				  				</fo:table-body>
				  			</fo:table>
						</fo:block>
					</fo:static-content>
			 		<fo:flow flow-name="xsl-region-body">
			 			<fo:block>
			 					<xsl:call-template name="miseEnPage" />
			  			</fo:block>
				  	</fo:flow>
				</fo:page-sequence>
			</xsl:for-each>
		  </fo:root>
  	</xsl:template>
 
	<xsl:template name="before" >
		<xsl:call-template name="entete" /> 
  	</xsl:template>
  	
	<xsl:template name="entete" >
  		<fo:block >
 			<fo:table table-layout="fixed" width="100%" >
  				<fo:table-column column-width="proportional-column-width(1)" /> 
  				<fo:table-column column-width="proportional-column-width(1)" /> 
 				<fo:table-body>
  					<fo:table-row>
  						<fo:table-cell>
  							<fo:block text-align="left" font-size="{$para-font-size}" >
  								<xsl:value-of select="entete/etablissement"/>
  							</fo:block>
			  			</fo:table-cell>
  						<fo:table-cell>
  							<fo:block text-align="right" font-size="{$para-font-size}">
								<xsl:value-of select="entete/labelAnnee"/> : <xsl:value-of select="entete/annee"/>
  							</fo:block>
			  			</fo:table-cell>
  					</fo:table-row>
  				</fo:table-body>
  			</fo:table>
			<fo:table table-layout="fixed" width="100%" >
  				<fo:table-column column-width="proportional-column-width(0.5)" /> 
  				<fo:table-column column-width="proportional-column-width(1)" /> 
  				<fo:table-column column-width="proportional-column-width(0.5)" /> 
 				<fo:table-body>
  					<fo:table-row  vertical-align="middle">
  						<fo:table-cell>
	  						<fo:block>
				  			</fo:block>
  						</fo:table-cell>
			  			<fo:table-cell border="1pt solid black" background-color="#DDDDDD"  padding="4pt">
			 				<fo:block font-size="{$para-font-title-size}" font-weight="bold" text-align="center" >
			  						<xsl:value-of select="entete/labelRvn"/>
			  				</fo:block>
			  			</fo:table-cell>
  						<fo:table-cell>
	  						<fo:block>
				  			</fo:block>
  						</fo:table-cell>
			  		</fo:table-row>
  				</fo:table-body>
  			</fo:table>
  		</fo:block>
  		
  	</xsl:template>
  	
	<xsl:template name="miseEnPage">
		<fo:block>
			<fo:block font-size="{$para-font-header-size}" padding="3pt" font-style="italic">
				<fo:inline text-decoration="underline" font-weight="bold">
				Principe de notation ECTS :
				</fo:inline>
			</fo:block>
			<fo:block margin-bottom="30">
	 			<fo:table table-layout="fixed" width="80%" font-style="italic" >
	  				<fo:table-column column-width="proportional-column-width(1)" /> 
	  				<fo:table-column column-width="proportional-column-width(3)" /> 
	  				<fo:table-column column-width="proportional-column-width(2)" /> 
	 				<fo:table-body>
	  					<fo:table-row>
	  						<fo:table-cell border="1pt solid black" padding="2pt" >
	  							<fo:block text-align="center" font-size="{$para-font-size}"  font-weight="bold" >
	  								A
	  							</fo:block>
				  			</fo:table-cell>
	  						<fo:table-cell border="1pt solid black" padding="2pt" >
	  							<fo:block text-align="left" font-size="{$para-font-size}">
									10% des meilleurs résultats
	  							</fo:block>
				  			</fo:table-cell>
	  						<fo:table-cell border-top="1pt solid black"
	  							border-right="1pt solid black" padding="2pt" >
	  							<fo:block>
	  							</fo:block>
				  			</fo:table-cell>
	  					</fo:table-row>
	  					<fo:table-row>
	  						<fo:table-cell border="1pt solid black" padding="2pt" >
	  							<fo:block text-align="center" font-size="{$para-font-size}"  font-weight="bold" >
	  								B
	  							</fo:block>
				  			</fo:table-cell>
	  						<fo:table-cell border="1pt solid black" padding="2pt" >
	  							<fo:block text-align="left" font-size="{$para-font-size}">
									25% suivants
	  							</fo:block>
				  			</fo:table-cell>
	  						<fo:table-cell border-right="1pt solid black" padding="2pt" >
	  							<fo:block>
	  							</fo:block>
				  			</fo:table-cell>
	  					</fo:table-row>
	  					<fo:table-row>
	  						<fo:table-cell border="1pt solid black" padding="2pt" >
	  							<fo:block text-align="center" font-size="{$para-font-size}"  font-weight="bold" >
	  								C
	  							</fo:block>
				  			</fo:table-cell>
	  						<fo:table-cell border="1pt solid black" padding="2pt" >
	  							<fo:block text-align="left" font-size="{$para-font-size}">
									30% suivants
	  							</fo:block>
				  			</fo:table-cell>
	  						<fo:table-cell border-right="1pt solid black" padding="2pt" >
	  							<fo:block text-align="center" font-size="{$para-font-size}">
									Sur les admis
	  							</fo:block>
				  			</fo:table-cell>
	  					</fo:table-row>
	  					<fo:table-row>
	  						<fo:table-cell border="1pt solid black" padding="2pt" >
	  							<fo:block text-align="center" font-size="{$para-font-size}"  font-weight="bold" >
	  								D
	  							</fo:block>
				  			</fo:table-cell>
	  						<fo:table-cell border="1pt solid black" padding="2pt" >
	  							<fo:block text-align="left" font-size="{$para-font-size}">
									25% suivants
	  							</fo:block>
				  			</fo:table-cell>
	  						<fo:table-cell border-right="1pt solid black" padding="2pt" >
	  							<fo:block>
	  							</fo:block>
				  			</fo:table-cell>
	  					</fo:table-row>
	  					<fo:table-row>
	  						<fo:table-cell border="1pt solid black" padding="2pt" >
	  							<fo:block text-align="center" font-size="{$para-font-size}"  font-weight="bold" >
	  								E
	  							</fo:block>
				  			</fo:table-cell>
	  						<fo:table-cell border="1pt solid black" padding="2pt" >
	  							<fo:block text-align="left" font-size="{$para-font-size}">
									10% suivants
	  							</fo:block>
				  			</fo:table-cell>
	  						<fo:table-cell border-bottom="1pt solid black" border-right="1pt solid black" padding="2pt" >
	  							<fo:block>
	  							</fo:block>
				  			</fo:table-cell>
	  					</fo:table-row>			  		
	  					<fo:table-row>
	  						<fo:table-cell border="1pt solid black" padding="2pt" >
	  							<fo:block text-align="center" font-size="{$para-font-size}"  font-weight="bold" >
	  								Fx
	  							</fo:block>
				  			</fo:table-cell>
	  						<fo:table-cell border="1pt solid black" padding="2pt" >
	  							<fo:block text-align="left" font-size="{$para-font-size}">
									Moyenne observée se situe entre 8 et 9,999
	  							</fo:block>
				  			</fo:table-cell>
	  						<fo:table-cell border-top="1pt solid black"
	  							border-right="1pt solid black" padding="2pt" >
	  							<fo:block>
	  							</fo:block>
				  			</fo:table-cell>
	  					</fo:table-row>
	 					<fo:table-row>
	  						<fo:table-cell border="1pt solid black" padding="2pt" >
	  							<fo:block text-align="center" font-size="{$para-font-size}"  font-weight="bold" >
	  								F
	  							</fo:block>
				  			</fo:table-cell>
	  						<fo:table-cell border="1pt solid black" padding="2pt" >
	  							<fo:block text-align="left" font-size="{$para-font-size}">
									Moyenne observée se situe entre 0 et 7,999
	  							</fo:block>
				  			</fo:table-cell>
	  						<fo:table-cell border-bottom="1pt solid black" border-right="1pt solid black" padding="2pt" >
	  							<fo:block text-align="center" font-size="{$para-font-size}">
									Sur les ajournés
	  							</fo:block>
				  			</fo:table-cell>
	  					</fo:table-row>
	   				</fo:table-body>
	  			</fo:table>
	  		</fo:block>
  		</fo:block>
  		<xsl:apply-templates select="rvn" /> 
  	</xsl:template>

	<xsl:template name="rvn"  >
  		<xsl:apply-templates select="infosRvn" /> 
		<xsl:apply-templates select="listeElp" />		
	</xsl:template>

 	<xsl:template match="infosRvn">
			
		<fo:block font-size="{$para-font-header-size}" padding="3pt">
			<fo:inline font-weight="bold">
				<xsl:value-of select="nom"/>
			</fo:inline>
		</fo:block>
		
		<fo:block font-size="{$para-font-size}" padding="3pt">
			<fo:inline font-weight="bold">
				<xsl:value-of select="labelnoetu"/> :
			</fo:inline>
			<xsl:value-of select="noetu"/>
			<fo:inline font-weight="bold" padding-left="50pt">
				<xsl:value-of select="labeline"/> :
			</fo:inline>
			<xsl:value-of select="ine"/>
		</fo:block>

		<fo:block font-size="{$para-font-size}" padding="3pt">
			<xsl:value-of select="labeldatenais"/> :
			<xsl:value-of select="datenais"/>
		</fo:block>
			
		<fo:block font-size="{$para-font-header-size}" padding="3pt" margin-bottom="15pt" >
			<fo:inline font-weight="bold">
				<xsl:value-of select="etape"/>
			</fo:inline>
		</fo:block>
 		
	 	<fo:block font-size="{$para-font-size}" padding="1pt">
			<fo:inline font-weight="bold">
				<xsl:value-of select="resultat/labelCreditsVet"/> :
			</fo:inline>
			<xsl:value-of select="resultat/creditsVet1"/>
		</fo:block>

		<fo:table table-layout="fixed" width="100%" margin-bottom="15pt" >
	 			<fo:table-column
	 				column-width="proportional-column-width(1)" />
	 			<fo:table-column
	 				column-width="proportional-column-width(1)" />
	 			
	 			<fo:table-body>
	 			
 					<fo:table-row >
 					
	 					<fo:table-cell padding="1pt" >
						 	<fo:block font-size="{$para-font-size}" padding="1pt">
								<fo:inline font-weight="bold">
									<xsl:value-of select="resultat/labelNoteVet1"/> :
								</fo:inline>
								<xsl:value-of select="resultat/noteVet1"/>
							</fo:block>

						 	<fo:block font-size="{$para-font-size}" padding="1pt">
								<fo:inline font-weight="bold">
									<xsl:value-of select="resultat/labelNoteEctsVet1"/> :
								</fo:inline>
								<xsl:value-of select="resultat/noteEctsVet1"/>
							</fo:block>
						</fo:table-cell>
					
 					
	 					<fo:table-cell padding="1pt" >
	 						<fo:block/>
	 					
						 	<fo:block font-size="{$para-font-size}" padding="1pt">
								<fo:inline font-weight="bold">
									<xsl:value-of select="resultat/labelNoteVet2"/> :
								</fo:inline>
								<xsl:value-of select="resultat/noteVet2"/>
							</fo:block>

						 	<fo:block font-size="{$para-font-size}" padding="1pt">
								<fo:inline font-weight="bold">
									<xsl:value-of select="resultat/labelNoteEctsVet2"/> :
								</fo:inline>
								<xsl:value-of select="resultat/noteEctsVet2"/>
							</fo:block>

						</fo:table-cell>
					
					</fo:table-row>
					
				</fo:table-body>
	 	</fo:table>
	 			
 	</xsl:template>
 	
 	<xsl:template match="listeElp">
 		<xsl:if test="*" >
	 		<fo:table table-layout="fixed" width="100%" >
	 			<fo:table-column
	 				column-width="proportional-column-width(6)" />
	 			<fo:table-column
	 				column-width="proportional-column-width(1)" />
	 			<fo:table-column
	 				column-width="proportional-column-width(1)" />
	 			<fo:table-column
	 				column-width="proportional-column-width(1)" />
	 			<fo:table-column
	 				column-width="proportional-column-width(1)" />
	 			<fo:table-column
	 				column-width="proportional-column-width(1)" />
	 			<fo:table-column
	 				column-width="proportional-column-width(1)" />
	 			
	 			<fo:table-header>
					<fo:table-row font-weight="bold" font-size="{$para-font-header-size}" text-align="center" background-color="#EEEEEE">
						<fo:table-cell border="1pt solid black" padding="2pt" number-columns-spanned="3" display-align="center" >
							<fo:block text-align="center" >
								<xsl:value-of select="labelUE"/>
							</fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" number-columns-spanned="2" display-align="center" >
							<fo:block text-align="center">
								<xsl:value-of select="labelSession1"/>
							</fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" number-columns-spanned="2" display-align="center" >
							<fo:block text-align="center" >
								<xsl:value-of select="labelSession2"/>
							</fo:block> 
						</fo:table-cell>
					</fo:table-row>
					
					<fo:table-row font-weight="bold" font-size="{$para-font-size}" text-align="center" background-color="#EEEEEE">
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block />
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" ><xsl:value-of select="labelCredits"/></fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" ><xsl:value-of select="labelCreditsAcquis"/></fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" ><xsl:value-of select="labelNoteElp"/></fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" ><xsl:value-of select="labelNoteEctsElp"/></fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" ><xsl:value-of select="labelNoteElp"/></fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" ><xsl:value-of select="labelNoteEctsElp"/></fo:block> 
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
	 			
	 			<fo:table-body width="100%">
	 			
	 				<xsl:for-each select="elp">
	 				
	 					<fo:table-row >
	 					
		 					<fo:table-cell border-left="1pt solid black" border-top="1pt solid black" border-bottom="1pt solid black" 
		 						padding="1pt" display-align="center">
		 						<fo:block font-size="{$para-font-size}" text-align="left" >
		 							<xsl:value-of select="ue"/>
		 						</fo:block>
							</fo:table-cell>
							
		 					<fo:table-cell border-left="1pt solid black" border-top="1pt solid black" border-bottom="1pt solid black" 
		 						padding="1pt" display-align="center">
		 						<fo:block font-size="{$para-font-size}" text-align="center" >
		 							<xsl:value-of select="credits1"/>
		 						</fo:block>
							</fo:table-cell>
							
							<fo:table-cell border-left="1pt solid black" border-top="1pt solid black" border-bottom="1pt solid black" 
		 						padding="1pt" display-align="center">
		 						<fo:block font-size="{$para-font-size}" text-align="center" >
		 							<xsl:variable name="validEcts" select="'ABCDE'"/>
		 							<xsl:variable name="nEcts1" select="noteEcts1"/>
		 							<xsl:variable name="nEcts2" select="noteEcts2"/>
		 							<xsl:choose>
			 							<xsl:when test="($nEcts1!='' and contains($validEcts,$nEcts1)) or ($nEcts2!='' and contains($validEcts,$nEcts2))">
			 								<xsl:value-of select="credits1"/>
			 							</xsl:when>
			 							<xsl:otherwise>
			 								&#160;
			 							</xsl:otherwise>
		 							</xsl:choose>		 							
		 						</fo:block>
							</fo:table-cell>
							
		 					<fo:table-cell border-left="1pt solid black" border-top="1pt solid black" border-bottom="1pt solid black" 
		 						padding="1pt" display-align="center">
		 						<fo:block font-size="{$para-font-size}" text-align="center" >
		 							<xsl:value-of select="note1"/>
		 						</fo:block>
							</fo:table-cell>
							
		 					<fo:table-cell border-left="1pt solid black" border-top="1pt solid black" border-bottom="1pt solid black" 
		 						padding="1pt" display-align="center">
		 						<fo:block font-size="{$para-font-size}" text-align="center" >
		 							<xsl:value-of select="noteEcts1"/>
		 						</fo:block>
							</fo:table-cell>
							
		 					<fo:table-cell border-left="1pt solid black" border-top="1pt solid black" border-bottom="1pt solid black" 
		 						padding="1pt" display-align="center">
		 						<fo:block font-size="{$para-font-size}" text-align="center" >
		 							<xsl:value-of select="note2"/>
		 						</fo:block>
							</fo:table-cell>
							
		 					<fo:table-cell border="1pt solid black"   
		 						padding="1pt" display-align="center">
		 						<fo:block font-size="{$para-font-size}" text-align="center" >
		 							<xsl:value-of select="noteEcts2"/>
		 						</fo:block>
							</fo:table-cell>
	 			
	 					</fo:table-row>
	 					
	 				</xsl:for-each>
	 				
	 			</fo:table-body>
	 			
	 		</fo:table>

			<fo:block font-size="{$para-font-size}" text-align="left" margin="5pt" >
				<xsl:value-of select="legende"/>
			</fo:block>
		
			<fo:table table-layout="fixed" width="100%" margin-right="15pt" margin-top="5pt" >
  				<fo:table-column column-width="proportional-column-width(1)" /> 
 				<fo:table-body>
  					<fo:table-row>
  						<fo:table-cell>
  							<fo:block font-size="{$para-font-header-size}" text-align="right"  padding="5pt">
                                <fo:inline font-weight="bold">
                                    <xsl:value-of select="qualiteSignataire"/>
                                </fo:inline>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block font-size="{$para-font-header-size}" text-align="right"  padding="5pt">
                                <fo:inline font-weight="bold">
                                    <xsl:value-of select="nomSignataire"/>
                                </fo:inline>
  							</fo:block>
  						</fo:table-cell>
			  		</fo:table-row>
  				</fo:table-body>
  			</fo:table>
		
	 	</xsl:if>
 		
  		<fo:block break-after="page" />
		 
	</xsl:template>
  	
	
 </xsl:stylesheet>
