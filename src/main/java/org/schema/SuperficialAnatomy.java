package org.schema;

/**
 * 
 * Anatomical features that can be observed by sight (without dissection),
 * including the form and proportions of the human body as well as surface
 * landmarks that correspond to deeper subcutaneous structures. Superficial
 * anatomy plays an important role in sports medicine, phlebotomy, and other
 * medical specialties as underlying anatomical structures can be identified
 * through surface palpation. For example, during back surgery, superficial
 * anatomy can be used to palpate and count vertebrae to find the site of
 * incision. Or in phlebotomy, superficial anatomy can be used to locate an
 * underlying vein; for example, the median cubital vein can be located by
 * palpating the borders of the cubital fossa (such as the epicondyles of the
 * humerus) and then looking for the superficial signs of the vein, such as
 * size, prominence, ability to refill after depression, and feel of surrounding
 * tissue support. As another example, in a subluxation (dislocation) of the
 * glenohumeral joint, the bony structure becomes pronounced with the deltoid
 * muscle failing to cover the glenohumeral joint allowing the edges of the
 * scapula to be superficially visible. Here, the superficial anatomy is the
 * visible edges of the scapula, implying the underlying dislocation of the
 * joint (the related anatomical structure).
 * 
 * @fullPath Thing > MedicalEntity > SuperficialAnatomy
 * 
 * @author Texelz (by Onhate)
 * 
 */
public class SuperficialAnatomy extends MedicalEntity {

	private String associatedPathophysiology;
	private Object relatedAnatomy;
	private MedicalCondition relatedCondition;
	private MedicalTherapy relatedTherapy;
	private String significance;

	/**
	 * If applicable, a description of the pathophysiology associated with the
	 * anatomical system, including potential abnormal changes in the
	 * mechanical, physical, and biochemical functions of the system.
	 */
	public String getAssociatedPathophysiology() {
		return this.associatedPathophysiology;
	}

	/**
	 * If applicable, a description of the pathophysiology associated with the
	 * anatomical system, including potential abnormal changes in the
	 * mechanical, physical, and biochemical functions of the system.
	 */
	public void setAssociatedPathophysiology(String associatedPathophysiology) {
		this.associatedPathophysiology = associatedPathophysiology;
	}

	/**
	 * Anatomical systems or structures that relate to the superficial anatomy.
	 * 
	 * @see AnatomicalSystem
	 * @see AnatomicalStructure
	 */
	public Object getRelatedAnatomy() {
		return this.relatedAnatomy;
	}

	/**
	 * Anatomical systems or structures that relate to the superficial anatomy.
	 * 
	 * @see AnatomicalSystem
	 * @see AnatomicalStructure
	 */
	public void setRelatedAnatomy(Object relatedAnatomy) {
		this.relatedAnatomy = relatedAnatomy;
	}

	/**
	 * A medical condition associated with this anatomy.
	 */
	public MedicalCondition getRelatedCondition() {
		return this.relatedCondition;
	}

	/**
	 * A medical condition associated with this anatomy.
	 */
	public void setRelatedCondition(MedicalCondition relatedCondition) {
		this.relatedCondition = relatedCondition;
	}

	/**
	 * A medical therapy related to this anatomy.
	 */
	public MedicalTherapy getRelatedTherapy() {
		return this.relatedTherapy;
	}

	/**
	 * A medical therapy related to this anatomy.
	 */
	public void setRelatedTherapy(MedicalTherapy relatedTherapy) {
		this.relatedTherapy = relatedTherapy;
	}

	/**
	 * The significance associated with the superficial anatomy; as an example,
	 * how characteristics of the superficial anatomy can suggest underlying
	 * medical conditions or courses of treatment.
	 */
	public String getSignificance() {
		return this.significance;
	}

	/**
	 * The significance associated with the superficial anatomy; as an example,
	 * how characteristics of the superficial anatomy can suggest underlying
	 * medical conditions or courses of treatment.
	 */
	public void setSignificance(String significance) {
		this.significance = significance;
	}
}
