package com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Country implements Serializable, IModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(unique = true)
	private String iso3Name;

	public String getName(Locale locale) {
		return Arrays.stream(Locale.getAvailableLocales())
		             .filter(l -> {
			             try {
				             return l.getISO3Country().equals(this.iso3Name);
			             } catch (MissingResourceException e) {
				             return l.getCountry().equals(this.iso3Name);
			             }
		             })
		             .map(l -> l.getDisplayCountry(locale))
		             .findFirst()
		             .orElse(null);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.iso3Name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Country country)) return false;
		return Objects.equals(this.iso3Name, country.iso3Name);
	}

	@Override
	public String toString() {
		return this.getName(Locale.ENGLISH);
	}
}
