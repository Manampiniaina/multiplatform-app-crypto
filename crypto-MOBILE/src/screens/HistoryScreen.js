import React, { useState, useEffect } from 'react';
import {View, Text, TouchableOpacity, StyleSheet, ScrollView, ActivityIndicator} from 'react-native';
import NavBar from '../components/NavBar';
import Header from '../components/Header';
import theme from '../styles/theme';
import { useUser} from "../contexts/Context";
import {getAllTransactionsAchats, getAllTransactionsVentes} from "../services/TransactionCryptoService";
import {convertFirestoreTimestampToDate} from "../services/UtiliService";

const HistoryScreen = () => {
    const { utilisateur } = useUser();
    const [selectedTab, setSelectedTab] = useState('achat');
    const [purchaseHistory, setPurchaseHistory] = useState([]);
    const [saleHistory, setSaleHistory] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (utilisateur && utilisateur.id) {
            fetchHistories(utilisateur.id);
        }
    }, [utilisateur]);

    const fetchHistories = async (userId) => {
        setLoading(true);
        try {
            const achats = await getAllTransactionsAchats(utilisateur.id);
            const ventes = await getAllTransactionsVentes(utilisateur.id);

            setPurchaseHistory(formatTransactions(achats));
            setSaleHistory(formatTransactions(ventes));
        } catch (error) {
            console.error("Erreur lors de la récupération des transactions:", error);
        }
        setLoading(false);
    };
    const formatTransactions = (transactions) => {
        return transactions.map((transaction, i) => {
            console.log("transaction.idcrypto",transaction.nomCrypto);
            console.log("date changement:"+transaction.dateChangement);
            return {
                id: i, // Utilisation de l'indice i comme id
                date: convertFirestoreTimestampToDate(transaction.dateTransaction) ,
                amount: transaction.quantite, // Quantité au lieu du prix
                description: transaction.nomCrypto ? transaction.nomCrypto : "Crypto inconnue"
            };
        });
    };

    const renderHistory = () => {
        const history = selectedTab === 'achat' ? purchaseHistory : saleHistory;

        return (
            <View>
                <View style={styles.historyHeader}>
                    <Text style={styles.columnTitle}>Crypto Et DateHeure</Text>
                    <Text style={styles.columnTitle}>Quantité</Text>
                </View>
                {history.map((item) => (
                    <View key={item.id} style={styles.historyItem}>
                        <Text style={styles.historyDescription}>{item.description}</Text>
                        <View style={styles.historyItemHeader}>
                            <Text style={styles.historyDate}>{item.date}</Text>
                            <Text style={[
                                styles.historyAmount,
                                selectedTab === 'achat' ? styles.positiveChange : styles.positiveChange
                            ]}>
                {selectedTab === 'achat' ? '' : ''} {Math.abs(item.amount)}
</Text>
                        </View>
                    </View>
                ))}
            </View>
        );
    };


    return (
        <View style={styles.container}>
            <Header title="Historique" />

            <View style={styles.tabsContainer}>
                <TouchableOpacity
                    style={[styles.tabButton, selectedTab === 'achat' && styles.activeTab]}
                    onPress={() => setSelectedTab('achat')}
                >
                    <Text style={[styles.tabText, selectedTab === 'achat' && styles.activeTabText]}>Achats</Text>
                </TouchableOpacity>
                <TouchableOpacity
                    style={[styles.tabButton, selectedTab === 'vente' && styles.activeTab]}
                    onPress={() => setSelectedTab('vente')}
                >
                    <Text style={[styles.tabText, selectedTab === 'vente' && styles.activeTabText]}>Ventes</Text>
                </TouchableOpacity>
            </View>

            {loading && <ActivityIndicator size="large" color={theme.colors.primary} style={styles.loadingIndicator} />}

            <ScrollView style={styles.historyContainer} showsVerticalScrollIndicator={false}>
                {renderHistory()}
            </ScrollView>

            <NavBar activeScreen="History" />
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: theme.colors.background,
        padding: 20,
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
        color: theme.colors.text,
        fontSize: 16,
        fontWeight: '600',
    },
    activeTabText: {
        color: theme.colors.background,
        fontWeight: '700',
    },
    historyContainer: {
        flex: 1,
    },
    historyItem: {
        backgroundColor: theme.colors.surface,
        padding: 15,
        marginBottom: 10,
        borderRadius: 10,
    },
    historyItemHeader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginBottom: 5,
    },
    historyDate: {
        color: theme.colors.text,
        fontSize: 14,
        marginTop:10,
    },
    historyAmount: {
        fontSize: 16,
        fontWeight: '700',
        padding: 6,
        marginBottom:10,
        borderRadius: 6,
    },
    positiveChange: {
        color: theme.colors.success,
    },
    negativeChange: {
        color: theme.colors.error,
    },
    historyDescription: {
        color: theme.colors.primary,
        fontSize: 20,
    },
    emptyMessage: {
        textAlign: 'center',
        color: theme.colors.text,
        fontSize: 16,
        marginVertical: 20,
    },
    loadingIndicator: {
        marginBottom: 10,
        alignSelf: 'center',
    },
    historyHeader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        paddingVertical: 10,
        borderBottomWidth: 1,
        borderBottomColor: theme.colors.text,
        marginBottom: 10,
    },
    columnTitle: {
        fontSize: 16,
        fontWeight: 'bold',
        color: theme.colors.primary,
    },

});

export default HistoryScreen;
