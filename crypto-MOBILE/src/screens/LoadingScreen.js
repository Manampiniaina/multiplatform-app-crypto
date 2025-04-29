import React from 'react';
import { View, ActivityIndicator, Text, StyleSheet } from 'react-native';
import theme from '../styles/theme';

const LoadingScreen = () => {
    return (
        <View style={[styles.container, { backgroundColor: theme.colors.background }]}>
            <ActivityIndicator size="large" color={theme.colors.primary} />
            <Text style={[styles.text, { color: theme.colors.primary }]}>Chargement en cours...</Text>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    text: {
        marginTop: 10,
        fontSize: 18,
        fontWeight: 'bold',
    },
});

export default LoadingScreen;