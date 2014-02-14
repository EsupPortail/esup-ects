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
			<xsl:for-each select="doc">
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
				  								<!-- <xsl:value-of select="dateedit"/> -->
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
				  								<!-- <xsl:value-of select="codeetape"/> -->
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
  								<!-- <xsl:value-of select="entete/etablissement"/> -->
  							</fo:block>
			  			</fo:table-cell>
  						<fo:table-cell>
  							<fo:block text-align="right" font-size="{$para-font-size}">
								Année universitaire : <xsl:value-of select="entete/annee"/>
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
			  						Distribution ECTS
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
  		<xsl:apply-templates select="vet" /> 
  		<xsl:apply-templates select="listeElp" /> 
  	</xsl:template>
  	
  	 <xsl:template match="vet">
 		<fo:table table-layout="fixed" width="100%" >
 			<fo:table-column
 				column-width="proportional-column-width(5)" />
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
					<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
						<fo:block />
					</fo:table-cell>
					<fo:table-cell border="1pt solid black" padding="2pt" number-columns-spanned="2" display-align="center" >
						<fo:block text-align="center">
							note E
						</fo:block> 
					</fo:table-cell>
					<fo:table-cell border="1pt solid black" padding="2pt" number-columns-spanned="2" display-align="center" >
						<fo:block text-align="center" >
							note D
						</fo:block> 
					</fo:table-cell>
					<fo:table-cell border="1pt solid black" padding="2pt" number-columns-spanned="2" display-align="center" >
						<fo:block text-align="center" >
							note C
						</fo:block> 
					</fo:table-cell>
					<fo:table-cell border="1pt solid black" padding="2pt" number-columns-spanned="2" display-align="center" >
						<fo:block text-align="center" >
							note B
						</fo:block> 
					</fo:table-cell>
					<fo:table-cell border="1pt solid black" padding="2pt" number-columns-spanned="2" display-align="center" >
						<fo:block text-align="center" >
							note A
						</fo:block> 
					</fo:table-cell>
				</fo:table-row>
				
				<fo:table-row font-weight="bold" font-size="{$para-font-size}" text-align="center" background-color="#EEEEEE">
					<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
						<fo:block text-align="center" >
							Etape
						</fo:block> 
					</fo:table-cell>
					<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
						<fo:block text-align="center" >mini</fo:block> 
					</fo:table-cell>
					<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
						<fo:block text-align="center" >maxi</fo:block> 
					</fo:table-cell>
					<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
						<fo:block text-align="center" >mini</fo:block> 
					</fo:table-cell>
					<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
						<fo:block text-align="center" >maxi</fo:block> 
					</fo:table-cell>
					<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
						<fo:block text-align="center" >mini</fo:block> 
					</fo:table-cell>
					<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
						<fo:block text-align="center" >maxi</fo:block> 
					</fo:table-cell>
					<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
						<fo:block text-align="center" >mini</fo:block> 
					</fo:table-cell>
					<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
						<fo:block text-align="center" >maxi</fo:block> 
					</fo:table-cell>
					<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
						<fo:block text-align="center" >mini</fo:block> 
					</fo:table-cell>
					<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
						<fo:block text-align="center" >maxi</fo:block> 
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
 			
 			<fo:table-body width="100%">
 			
				<fo:table-row >
					
 					<fo:table-cell border="1pt solid black"
 						padding="1pt" display-align="center">
 						<fo:block font-size="{$para-font-size}" font-weight="bold" text-align="left" >
 							<xsl:value-of select="libvet"/>
 						</fo:block>
					</fo:table-cell>
					
					<xsl:apply-templates select="listeNotesVet" />
					
				</fo:table-row>
 					
 			</fo:table-body>
 			
 		</fo:table>
 		
	</xsl:template>
	
	<xsl:template match="listeNotesVet">
	
		<fo:table-cell border="1pt solid black"
			padding="1pt" display-align="center">
			<fo:block font-size="{$para-font-size}" text-align="center" >
				<xsl:value-of select="noteEctsE/noteMini"/>
			</fo:block>
		</fo:table-cell>
		
		<fo:table-cell border="1pt solid black"
			padding="1pt" display-align="center">
			<fo:block font-size="{$para-font-size}" text-align="center" >
				<xsl:value-of select="noteEctsE/noteMaxi"/>
			</fo:block>
		</fo:table-cell>
	
		<fo:table-cell border="1pt solid black"
			padding="1pt" display-align="center">
			<fo:block font-size="{$para-font-size}" text-align="center" >
				<xsl:value-of select="noteEctsD/noteMini"/>
			</fo:block>
		</fo:table-cell>
		
		<fo:table-cell border="1pt solid black"
			padding="1pt" display-align="center">
			<fo:block font-size="{$para-font-size}" text-align="center" >
				<xsl:value-of select="noteEctsD/noteMaxi"/>
			</fo:block>
		</fo:table-cell>
																		
		<fo:table-cell border="1pt solid black"
			padding="1pt" display-align="center">
			<fo:block font-size="{$para-font-size}" text-align="center" >
				<xsl:value-of select="noteEctsC/noteMini"/>
			</fo:block>
		</fo:table-cell>
		
		<fo:table-cell border="1pt solid black"
			padding="1pt" display-align="center">
			<fo:block font-size="{$para-font-size}" text-align="center" >
				<xsl:value-of select="noteEctsC/noteMaxi"/>
			</fo:block>
		</fo:table-cell>
								
		<fo:table-cell border="1pt solid black"
			padding="1pt" display-align="center">
			<fo:block font-size="{$para-font-size}" text-align="center" >
				<xsl:value-of select="noteEctsB/noteMini"/>
			</fo:block>
		</fo:table-cell>
		
		<fo:table-cell border="1pt solid black"
			padding="1pt" display-align="center">
			<fo:block font-size="{$para-font-size}" text-align="center" >
				<xsl:value-of select="noteEctsB/noteMaxi"/>
			</fo:block>
		</fo:table-cell>
								
		<fo:table-cell border="1pt solid black"
			padding="1pt" display-align="center">
			<fo:block font-size="{$para-font-size}" text-align="center" >
				<xsl:value-of select="noteEctsA/noteMini"/>
			</fo:block>
		</fo:table-cell>
		
		<fo:table-cell border="1pt solid black"
			padding="1pt" display-align="center">
			<fo:block font-size="{$para-font-size}" text-align="center" >
				<xsl:value-of select="noteEctsA/noteMaxi"/>
			</fo:block>
		</fo:table-cell>
								
	</xsl:template>

 	<xsl:template match="listeElp">
 		<xsl:if test="*" >
	 		<fo:table table-layout="fixed" width="100%" >
	 			<fo:table-column
	 				column-width="proportional-column-width(5)" />
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
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block />
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" number-columns-spanned="2" display-align="center" >
							<fo:block text-align="center">
								note E
							</fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" number-columns-spanned="2" display-align="center" >
							<fo:block text-align="center" >
								note D
							</fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" number-columns-spanned="2" display-align="center" >
							<fo:block text-align="center" >
								note C
							</fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" number-columns-spanned="2" display-align="center" >
							<fo:block text-align="center" >
								note B
							</fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" number-columns-spanned="2" display-align="center" >
							<fo:block text-align="center" >
								note A
							</fo:block> 
						</fo:table-cell>
					</fo:table-row>
					
					<fo:table-row font-weight="bold" font-size="{$para-font-size}" text-align="center" background-color="#EEEEEE">
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" >
								Elément
							</fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" >mini</fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" >maxi</fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" >mini</fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" >maxi</fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" >mini</fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" >maxi</fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" >mini</fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" >maxi</fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" >mini</fo:block> 
						</fo:table-cell>
						<fo:table-cell border="1pt solid black" padding="2pt" display-align="center" >
							<fo:block text-align="center" >maxi</fo:block> 
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
	 			
	 			<fo:table-body width="100%">
	 			
	 				<xsl:for-each select="elp">
	 				
	 					<fo:table-row >
	 					
		 					<fo:table-cell border="1pt solid black"
		 						padding="1pt" display-align="center">
		 						<fo:block font-size="{$para-font-size}" text-align="left" >
		 							<xsl:value-of select="ue"/>
		 						</fo:block>
							</fo:table-cell>
							
							<xsl:apply-templates select="listeNotes" />
							
	 					</fo:table-row>
	 					
	 				</xsl:for-each>
	 				
	 			</fo:table-body>
	 			
	 		</fo:table>

	 	</xsl:if>
 		
  		<fo:block break-after="page" />
		 
	</xsl:template>
	
	 <xsl:template match="listeNotes">
			<fo:table-cell border="1pt solid black"
				padding="1pt" display-align="center">
				<fo:block font-size="{$para-font-size}" text-align="center" >
					<xsl:value-of select="noteEctsE/noteMini"/>
				</fo:block>
			</fo:table-cell>
			
			<fo:table-cell border="1pt solid black"
				padding="1pt" display-align="center">
				<fo:block font-size="{$para-font-size}" text-align="center" >
					<xsl:value-of select="noteEctsE/noteMaxi"/>
				</fo:block>
			</fo:table-cell>
									
			<fo:table-cell border="1pt solid black"
				padding="1pt" display-align="center">
				<fo:block font-size="{$para-font-size}" text-align="center" >
					<xsl:value-of select="noteEctsD/noteMini"/>
				</fo:block>
			</fo:table-cell>
			
			<fo:table-cell border="1pt solid black"
				padding="1pt" display-align="center">
				<fo:block font-size="{$para-font-size}" text-align="center" >
					<xsl:value-of select="noteEctsD/noteMaxi"/>
				</fo:block>
			</fo:table-cell>
									
			<fo:table-cell border="1pt solid black"
				padding="1pt" display-align="center">
				<fo:block font-size="{$para-font-size}" text-align="center" >
					<xsl:value-of select="noteEctsC/noteMini"/>
				</fo:block>
			</fo:table-cell>
			
			<fo:table-cell border="1pt solid black"
				padding="1pt" display-align="center">
				<fo:block font-size="{$para-font-size}" text-align="center" >
					<xsl:value-of select="noteEctsC/noteMaxi"/>
				</fo:block>
			</fo:table-cell>
									
			<fo:table-cell border="1pt solid black"
				padding="1pt" display-align="center">
				<fo:block font-size="{$para-font-size}" text-align="center" >
					<xsl:value-of select="noteEctsB/noteMini"/>
				</fo:block>
			</fo:table-cell>
			
			<fo:table-cell border="1pt solid black"
				padding="1pt" display-align="center">
				<fo:block font-size="{$para-font-size}" text-align="center" >
					<xsl:value-of select="noteEctsB/noteMaxi"/>
				</fo:block>
			</fo:table-cell>
									
			<fo:table-cell border="1pt solid black"
				padding="1pt" display-align="center">
				<fo:block font-size="{$para-font-size}" text-align="center" >
					<xsl:value-of select="noteEctsA/noteMini"/>
				</fo:block>
			</fo:table-cell>
			
			<fo:table-cell border="1pt solid black"
				padding="1pt" display-align="center">
				<fo:block font-size="{$para-font-size}" text-align="center" >
					<xsl:value-of select="noteEctsA/noteMaxi"/>
				</fo:block>
			</fo:table-cell>
									
	</xsl:template>
								 			
  	
	
 </xsl:stylesheet>
