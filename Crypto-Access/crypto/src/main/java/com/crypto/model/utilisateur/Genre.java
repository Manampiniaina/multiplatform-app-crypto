package com.crypto.model.utilisateur;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Genre {
    
    String id, libelle ;

    public Genre(String id){
        setId(id); 
    }

    public Genre(String id, String libelle){
        setId(id); setLibelle(libelle);
    }
}
