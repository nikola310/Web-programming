package beans;

import java.util.ArrayList;

public class Subforum {
	private String title;
	private String description;
	private String rules;
	//lista moderatora, korisnik sa indeksom 0 je odgovorni moderator
	private ArrayList<String> moderators;
	private String icon;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Subforum() {
		title = "";
		description = "";
		rules = "";
		moderators = new ArrayList<>();
		icon = "";
	}

	public Subforum(Subforum s) {
		title = s.title;
		description = s.description;
		rules = s.rules;
		moderators = s.moderators;
		icon = s.icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String naziv) {
		this.title = naziv;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String opis) {
		this.description = opis;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String pravila) {
		this.rules = pravila;
	}

	public ArrayList<String> getModerators() {
		return moderators;
	}

	public void setModerators(ArrayList<String> moderatori) {
		this.moderators = moderatori;
	}

}
