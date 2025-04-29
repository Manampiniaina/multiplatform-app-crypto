import { useNavigation } from '@react-navigation/native';
import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ImageBackground, Dimensions } from 'react-native';
import NavBar from '../components/NavBar';
import Header from "../components/Header";
import theme from '../styles/theme';
import { useUser } from "../contexts/Context";

const { height } = Dimensions.get('window'); // Récupère la hauteur de l'écran

export default function HomeScreen() {
  const { user, utilisateur } = useUser();
  console.log("user now :" + JSON.stringify(user));
  console.log("utilisateur now :" + JSON.stringify(utilisateur));
  const navigation = useNavigation();

  return (
    <View style={styles.container}>
      <Header title="Home" />

      {/* Texte Bienvenue + Bouton Se Déconnecter en dessous du Header */}
      <View style={styles.headerContent}>
        <Text style={styles.title}>Bienvenue {utilisateur.nom} {utilisateur.prenom} 🚀</Text>
        <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('Login')}>
          <Text style={styles.buttonText}>Se Déconnecter</Text>
        </TouchableOpacity>
      </View>

      {/* Image de fond bien centrée */}
      <View style={styles.backgroundContainer}>
        <ImageBackground 
          source={{ uri: 'https://res.cloudinary.com/ds4y4okg3/image/upload/v1739164025/mcmvujwgxquck53rsvrm.jpg' }} 
          style={styles.backgroundImage} 
          resizeMode="cover"
        />
      </View>

      <NavBar activeScreen="Home" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: theme.colors.background,
  },
  headerContent: {
    alignItems: 'center',
    paddingVertical: 15, // Espacement sous le header
    marginLeft: 10, // Ajoute une légère marge à gauche pour mieux centrer
  },
  title: {
    fontSize: 22,
    fontWeight: 'bold',
    color: theme.colors.primary,
    textAlign: 'center',
    marginBottom: 10, // Espacement avec le bouton
  },
  button: {
    backgroundColor: theme.colors.primary,
    paddingVertical: 12,
    paddingHorizontal: 25,
    borderRadius: 30,
  },
  buttonText: {
    color: theme.colors.text,
    fontSize: 16,
    fontWeight: '700',
    textAlign: 'center',
  },
  backgroundContainer: {
    flex: 1,
    justifyContent: 'center', // Centre verticalement l'image
    alignItems: 'center',
    marginLeft: 8, // Ajoute une légère marge à gauche
  },
  backgroundImage: {
    width: '97%', // Garde une bonne proportion
    height: height * 0.4, // 40% de la hauteur de l'écran
    borderRadius: 20,
    overflow: 'hidden',
  },
});
