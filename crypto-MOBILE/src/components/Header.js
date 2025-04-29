import React from 'react';
import { View, TouchableOpacity, Text, StyleSheet, Image, Platform, useWindowDimensions } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import Icon from 'react-native-vector-icons/FontAwesome5';
import theme from '../styles/theme';  // Importation du thème

const   Header = ({
                    title = 'Tableau de Bord',
                    profileImage = null,
                    showBackButton = false,
                    showLogoutButton = true,
                    onLogoutPress = null,
                    onBackPress = null
                }) => {
    const navigation = useNavigation();
    const { width } = useWindowDimensions();

    // Taille dynamique des icônes
    const iconSize = width > 400 ? 24 : 20;

    // Gestion de la déconnexion
    const handleLogout = onLogoutPress || (() => {

        navigation.reset({
            index: 0,
            routes: [{ name: 'Login' }],
        });
    });

    return (
        <View style={styles.headerContainer}>
            {/* Côté gauche - Bouton retour ou Image de profil */}
            <View style={styles.headerLeft}>
                {showBackButton ? (
                    <TouchableOpacity
                        style={styles.headerIcon}
                        onPress={onBackPress || (() => navigation.goBack())}
                    >
                        <Icon name="arrow-left" size={iconSize} color={theme.colors.text} />
                    </TouchableOpacity>
                ) : (
                    <TouchableOpacity
                        style={styles.profileImageContainer}
                        onPress={() => navigation.navigate("Main", { screen: ("Profile") }) }
                    >
                        <Image
                            source={profileImage || require('../../icon/mipmap-xxxhdpi/ic_launcher_round.webp')}
                            style={styles.profileImage}
                        />
                    </TouchableOpacity>
                )}
            </View>

            {/* Titre central */}
            <View style={styles.headerCenter}>
                <Text style={styles.titleText} numberOfLines={1}>
                    {title}
                </Text>
            </View>

            {/* Bouton de déconnexion */}
            <View style={styles.headerRight}>
                {showLogoutButton && (
                    <TouchableOpacity
                        style={styles.headerIcon}
                        onPress={handleLogout}
                    >
                        <Icon name="sign-out-alt" size={iconSize} color={theme.colors.primary} />
                    </TouchableOpacity>
                )}
            </View>
        </View>
    );
};

const styles = StyleSheet.create({
    headerContainer: {
        borderRadius: 10,
        marginBottom: 20,
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        backgroundColor: theme.colors.surface,
        paddingHorizontal: 15,
        paddingVertical: 10,
        ...theme.shadow,
        ...Platform.select({
            ios: { paddingTop: 50 },
            android: { paddingTop: 20 }
        }),
    },
    headerLeft: {
        flex: 1,
        alignItems: 'flex-start',
    },
    headerCenter: {
        flex: 2,
        alignItems: 'center',
    },
    headerRight: {
        flex: 1,
        alignItems: 'flex-end',
    },
    headerIcon: {
        padding: 10,
    },
    profileImageContainer: {
        width: 45,
        height: 45,
        borderRadius: 22.5,
        overflow: 'hidden',
        borderWidth: 2,
        borderColor: theme.colors.primary,
    },
    profileImage: {
        width: '100%',
        height: '100%',
        resizeMode: 'cover',
    },
    titleText: {
        color: theme.colors.text,
        fontSize: 18,
        fontWeight: 'bold',
        textAlign: 'center',
        maxWidth: '90%',
    },
});

export default Header;
