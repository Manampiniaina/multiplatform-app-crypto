import React from 'react';
import {
    View,
    TouchableOpacity,
    StyleSheet,
    useWindowDimensions,
    Platform
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useNavigation } from '@react-navigation/native';
import Icon from 'react-native-vector-icons/FontAwesome5';
import theme from '../styles/theme'; // Importation du thème

const NavBar = ({ activeScreen = 'Home' }) => {
    const navigation = useNavigation();
    const { width } = useWindowDimensions();
    const insets = useSafeAreaInsets();

    // Calculs dynamiques
    const iconSize = width > 400 ? 24 : 20;
    const navItemWidth = width / 7;
    const containerWidth = Math.min(width * 0.95, 500);

    // Liste des écrans de navigation
    const navItems = [
        { name: 'Home', icon: 'home' },
        { name: 'Favoris', icon: 'star' },
        { name: 'Notifications', icon: 'bell' },
        { name: 'Operations', icon: 'exchange-alt' },
        { name: 'Profile', icon: 'user' },
        { name: 'History', icon: 'history' },
        { name: 'Cours', icon: 'chart-line' }
    ];

    return (
        <View style={[
            styles.navbarContainer,
            {
                width: containerWidth,
                alignSelf: 'center',
                bottom: Platform.select({
                    ios: insets.bottom,
                    android: 0
                })
            }
        ]}>
            <View style={styles.navbar}>
                {navItems.map((item) => (
                    <TouchableOpacity
                        key={item.name}
                        style={[
                            styles.navItem,
                            activeScreen === item.name && styles.activeNavItem
                        ]}
                        onPress={() => navigation.navigate("Main", { screen: (item.name) })}
                    >
                        <Icon
                            name={item.icon}
                            size={iconSize}
                            color="white"
                        />
                    </TouchableOpacity>
                ))}
            </View>
        </View>
    );
};

const styles = StyleSheet.create({
    navbarContainer: {
        position: 'absolute',
        bottom: 0,
        zIndex: 10,
        alignSelf: 'center',
        marginBottom: 10,
    },
    navbar: {
        flexDirection: 'row',
        justifyContent: 'space-evenly',
        alignItems: 'center',
        backgroundColor: theme.colors.surface,
        borderRadius: theme.borderRadius.large,
        paddingVertical: 10,
        paddingHorizontal: 10,
        ...theme.shadow,
    },
    navItem: {
        alignItems: 'center',
        justifyContent: 'center',
        paddingVertical: 10,
    },
    activeNavItem: {
        backgroundColor: theme.colors.primary,
        borderRadius: theme.borderRadius.medium,
        padding: 8,
    },
});

export default NavBar;
