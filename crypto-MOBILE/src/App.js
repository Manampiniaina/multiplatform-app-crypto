import React from 'react';
import { createStackNavigator } from '@react-navigation/stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { NavigationContainer } from '@react-navigation/native';
import { CryptoProvider, UserProvider, useUser } from './contexts/Context';

import LoginScreen from './screens/LoginScreen';
import HomeScreen from './screens/HomeScreen';
import FavorisScreen from './screens/FavorisScreen';
import ProfileScreen from './screens/ProfileScreen';
import HistoryScreen from "./screens/HistoryScreen";
import OperationsScreen from "./screens/OperationsScreen";
import NotificationsScreen from "./screens/NotificationsScreen";
import CoursScreen from "./screens/CoursScreen";

const Stack = createStackNavigator();
const Tab = createBottomTabNavigator();

const MainTabs = () => {
    const { user } = useUser();

    if (!user) {
        return <LoginScreen />;
    }

    return (
        <Tab.Navigator id={"TabNavigator"}
            screenOptions={{
                headerShown: false,
                tabBarStyle: { display: "none" }
            }}
        >
            <Tab.Screen name="Home" component={HomeScreen} />
            <Tab.Screen name="Favoris" component={FavorisScreen} />
            <Tab.Screen name="Profile" component={ProfileScreen} />
            <Tab.Screen name="History" component={HistoryScreen} />
            <Tab.Screen name="Operations" component={OperationsScreen} />
            <Tab.Screen name="Notifications" component={NotificationsScreen} />
            <Tab.Screen name="Cours" component={CoursScreen} />
        </Tab.Navigator>
    );
};

export default function App() {
    return (
        <UserProvider>
            <CryptoProvider>
                <NavigationContainer>
                    <Stack.Navigator screenOptions={{ headerShown: false }} id={"Navigator"}>
                        {/* Écran de connexion */}
                        <Stack.Screen name="Login" component={LoginScreen} />

                        {/* Navigation principale, avec protection */}
                        <Stack.Screen name="Main" component={MainTabs} />
                    </Stack.Navigator>
                </NavigationContainer>
            </CryptoProvider>
        </UserProvider>
    );
}
