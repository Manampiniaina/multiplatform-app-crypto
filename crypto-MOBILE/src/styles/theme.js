const theme = {
    colors: {
        background: '#121212',   // Fond principal plus sombre
        surface: '#1E1E1E',      // Surfaces et cartes
        primary: '#FFA726',      // Orange vif pour les éléments principaux
        secondary: '#424242',    // Éléments secondaires
        text: '#FFFFFF',
        textDark:'#121212',// Texte principal
        textSecondary: '#BDBDBD', // Texte secondaire
        error: '#EF5350',        // Rouge pour les sorties
        success: '#66BB6A',      // Vert pour les entrées
    },
    shadow: {
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 4 },
        shadowOpacity: 0.3,
        shadowRadius: 4,
        elevation: 6, // Pour Android
    },
    borderRadius: {
        small: 6,
        medium: 10,
        large: 12,
    },
    fontSize: {
        small: 14,
        medium: 16,
        large: 22,
    }
};

export default theme;
