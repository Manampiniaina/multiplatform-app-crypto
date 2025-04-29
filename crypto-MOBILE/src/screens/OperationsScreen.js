import React, { useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Modal, TextInput, Alert, ScrollView } from 'react-native';
import NavBar from '../components/NavBar';
import Header from "../components/Header";
import theme from '../styles/theme';
import {addOperation} from "../services/OperationsService";
import {useUser} from "../contexts/Context";

const OperationsScreen = () => {
    const [selectedTab, setSelectedTab] = useState('depot');
    const [amount, setAmount] = useState('');
    const [passwordModalVisible, setPasswordModalVisible] = useState(false);
    const {utilisateur} = useUser();
    const handleTransaction = () => {
        const parsedAmount = parseFloat(amount); // Convertit la valeur en nombre

        if (!amount) {
            Alert.alert('Erreur', 'Veuillez entrer un montant');
            return;
        }

        if (isNaN(parsedAmount) || parsedAmount <= 0) {
            Alert.alert('Erreur', 'Le montant doit être un nombre positif');
            return;
        }

        setPasswordModalVisible(true);
    };


    const confirmTransaction = () => {
       addOperation(utilisateur.id ,amount , selectedTab );
        setPasswordModalVisible(false);
    };

    return (
        <View style={styles.container}>
            <Header
                title="Opérations"
            />

            <Text style={styles.title}>Opérations Crypto</Text>
            <ScrollView style={styles.contentContainer}>
                <View style={styles.tabsContainer}>
                    <TouchableOpacity
                        style={[styles.tabButton, selectedTab === 'depot' && styles.activeTab]}
                        onPress={() => setSelectedTab('depot')}
                    >
                        <Text style={[styles.tabText, selectedTab === 'depot' && styles.activeTabText]}>Dépôt</Text>
                    </TouchableOpacity>
                    <TouchableOpacity
                        style={[styles.tabButton, selectedTab === 'retrait' && styles.activeTab]}
                        onPress={() => setSelectedTab('retrait')}
                    >
                        <Text style={[styles.tabText, selectedTab === 'retrait' && styles.activeTabText]}>Retrait</Text>
                    </TouchableOpacity>
                </View>

                <View style={styles.amountContainer}>
                    <TextInput
                        style={styles.amountInput}
                        placeholder={`Montant du ${selectedTab}`}
                        placeholderTextColor={theme.colors.textSecondary}
                        keyboardType="number-pad"
                        value={amount}
                        onChangeText={setAmount}
                    />
                </View>

                <TouchableOpacity style={styles.submitButton} onPress={handleTransaction}>
                    <Text style={styles.submitButtonText}>Confirmer {selectedTab}</Text>
                </TouchableOpacity>
            </ScrollView>

            <Modal
                animationType="slide"
                transparent={true}
                visible={passwordModalVisible}
                onRequestClose={() => setPasswordModalVisible(false)}
            >
                <View style={styles.modalContainer}>
                    <View style={styles.modalContent}>
                        <Text style={styles.modalTitle}>Confirmation</Text>

                        <View style={styles.modalButtonContainer}>
                            <TouchableOpacity
                                style={styles.modalButton}
                                onPress={() => setPasswordModalVisible(false)}
                            >
                                <Text style={styles.modalButtonText}>Annuler</Text>
                            </TouchableOpacity>
                            <TouchableOpacity
                                style={styles.modalButton}
                                onPress={confirmTransaction}
                            >
                                <Text style={styles.modalButtonText}>Confirmer</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </View>
            </Modal>

            <NavBar activeScreen="Operations" />
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: theme.colors.background,
        padding: 20,
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        color: theme.colors.primary,
        marginBottom: 20,
    },
    contentContainer: {
        flex: 1,
    },
    tabsContainer: {
        flexDirection: 'row',
        backgroundColor: theme.colors.surface,
        borderRadius: 10,
        marginBottom: 20,
    },
    tabButton: {
        flex: 1,
        paddingVertical: 12,
        alignItems: 'center',
    },
    activeTab: {
        backgroundColor: theme.colors.primary,
    },
    tabText: {
        color: theme.colors.textSecondary,
        fontSize: 16,
        fontWeight: '600',
    },
    activeTabText: {
        color: theme.colors.background,
        fontWeight: '700',
    },
    amountContainer: {
        backgroundColor: theme.colors.surface,
        borderRadius: 10,
        padding: 10,
        marginBottom: 20,
    },
    amountInput: {
        color: theme.colors.text,
        fontSize: 16,
        padding: 10,
    },
    submitButton: {
        backgroundColor: theme.colors.primary,
        padding: 15,
        borderRadius: 10,
        alignItems: 'center',
    },
    submitButtonText: {
        color: theme.colors.background,
        fontSize: 16,
        fontWeight: 'bold',
    },
    modalContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0,0,0,0.5)',
    },
    modalContent: {
        backgroundColor: theme.colors.surface,
        padding: 20,
        borderRadius: 10,
        width: '80%',
    },
    modalTitle: {
        color: theme.colors.text,
        fontSize: 18,
        marginBottom: 15,
        textAlign: 'center',
    },
    passwordInput: {
        backgroundColor: theme.colors.surface,
        color: theme.colors.text,
        padding: 10,
        borderRadius: 5,
        marginBottom: 15,
    },
    modalButtonContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    modalButton: {
        backgroundColor: theme.colors.primary,
        padding: 10,
        borderRadius: 5,
        width: '45%',
        alignItems: 'center',
    },
    modalButtonText: {
        color: theme.colors.background,
        fontWeight: 'bold',
    },
});

export default OperationsScreen;