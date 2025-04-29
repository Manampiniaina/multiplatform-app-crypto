import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, Image, ActivityIndicator } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import { useUser, useCryptos } from '../contexts/Context';
import theme from '../styles/theme';
import { handleLogin } from '../services/LoginService';
import { useNavigation } from "@react-navigation/native";
import LoadingScreen from './LoadingScreen';

export default function LoginScreen() {
  const navigation = useNavigation();
  const { loginUser } = useUser();
  const [email, setEmail] = useState('lucmartin@hotmail.com');
  const [password, setPassword] = useState('Luc02071988');
  const [secureText, setSecureText] = useState(true);
  const [emailError, setEmailError] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isInitializing, setIsInitializing] = useState(false);

  const handleSubmit = async () => {
    setIsLoading(true);
    setEmailError('');
    setPasswordError('');

    try {
      // Set initializing state before navigation
      setIsInitializing(true);

      // Perform login with full context initialization
      await handleLogin(
          email,
          password,
          loginUser,
          navigation,
          setEmailError,
          setPasswordError,
          setIsInitializing // Pass this to control initialization state
      );
    } catch (error) {
      console.error(error);
      setIsInitializing(false);
    } finally {
      setIsLoading(false);
    }
  };

  // If initializing, show LoadingScreen
  if (isInitializing) {
    return <LoadingScreen />;
  }

  return (
      <View style={[styles.container, { backgroundColor: theme.colors.background }]}>
        <Image style={styles.logo} source={require('../../icon/mipmap-xxxhdpi/ic_launcher_round.webp')} />
        <Text style={[styles.title, { color: theme.colors.primary }]}>CoinS Mada</Text>

        <TextInput
            style={[styles.input, { borderColor: theme.colors.primary, backgroundColor: theme.colors.surface, color: theme.colors.text }]}
            placeholder="Email"
            value={email}
            onChangeText={setEmail}
            placeholderTextColor={theme.colors.textSecondary}
        />
        {emailError ? <Text style={[styles.errorText, { color: theme.colors.error }]}>{emailError}</Text> : null}

        <View style={styles.passwordContainer}>
          <TextInput
              style={[styles.input, { borderColor: theme.colors.primary, backgroundColor: theme.colors.surface, color: theme.colors.text }]}
              placeholder="Password"
              secureTextEntry={secureText}
              value={password}
              onChangeText={setPassword}
              placeholderTextColor={theme.colors.textSecondary}
          />
          <TouchableOpacity onPress={() => setSecureText(!secureText)} style={styles.eyeIcon}>
            <Icon name={secureText ? 'eye-slash' : 'eye'} size={25} color={theme.colors.primary} />
          </TouchableOpacity>
        </View>
        {passwordError ? <Text style={[styles.errorText, { color: theme.colors.error }]}>{passwordError}</Text> : null}

        <TouchableOpacity
            style={[styles.button, { backgroundColor: theme.colors.primary }]}
            onPress={handleSubmit}
            disabled={isLoading}
        >
          {isLoading ? (
              <ActivityIndicator size="small" color={theme.colors.text} />
          ) : (
              <Text style={styles.buttonText}>CONNEXION</Text>
          )}
        </TouchableOpacity>
      </View>
  );
}


const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  logo: {
    width: 120,
    height: 120,
    marginBottom: 30,
  },
  title: {
    fontSize: 40,
    fontWeight: '700',
    marginBottom: 30,
    fontFamily: 'Roboto',
  },
  input: {
    width: '100%',
    height: 50,
    borderWidth: 1.5,
    borderRadius: 10,
    marginBottom: 15,
    paddingHorizontal: 15,
    fontSize: 16,
    fontFamily: 'Roboto',
  },
  passwordContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    width: '100%',
  },
  eyeIcon: {
    position: 'absolute',
    right: 20,
    top: 10,
  },
  button: {
    paddingVertical: 15,
    paddingHorizontal: 30,
    borderRadius: 30,
    marginTop: 20,
    justifyContent: 'center',
    alignItems: 'center',
  },
  buttonText: {
    color: '#121212',
    fontSize: 18,
    fontWeight: '700',
    textAlign: 'center',
    fontFamily: 'Roboto',
  },
  errorText: {
    marginBottom: 10,
    fontSize: 14,
    fontWeight: '500',
  },
});
