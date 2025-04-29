import React, { useState, useEffect } from 'react';
import {
    View,
    Text,
    FlatList,
    TouchableOpacity,
    Modal,
    StyleSheet,
    useWindowDimensions,
    Image,
    ActivityIndicator
} from 'react-native';
import { Picker } from '@react-native-picker/picker';
import Icon from 'react-native-vector-icons/FontAwesome';
import { useUser } from '../contexts/Context';
import { useCryptos } from '../contexts/Context';
import {getFavorisById, createFavori, deleteFavori, getFavorisCryptoById} from '../services/FavorisService';
import theme from '../styles/theme';
import Header from '../components/Header';
import NavBar from '../components/NavBar';
import {getCryptoByIdFromList, getImagePath} from "../services/CryptosService";
export default function FavorisScreen() {
    const { utilisateur } = useUser();
    const { cryptos } = useCryptos();
    const { height } = useWindowDimensions();
    const [favorites, setFavorites] = useState([]);
    const [nonFavorites, setNonFavorites] = useState([]);
    const [modalVisible, setModalVisible] = useState(false);
    const [selectedCrypto, setSelectedCrypto] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (utilisateur) {
            fetchFavorites();
        }
    }, [utilisateur, cryptos]);

    const fetchFavorites = async () => {
        setLoading(true);
        if (!utilisateur) return;
        const favoris = await getFavorisCryptoById(utilisateur.id, cryptos);
        setFavorites(favoris);

        // Filtrer les cryptos pour obtenir celles qui ne sont PAS encore en favoris
        const favorisIds = favoris.map(fav => fav.idCrypto);
        const filteredNonFavorites = cryptos.filter(crypto => !favorisIds.includes(crypto.idCrypto));
        setNonFavorites(filteredNonFavorites);
        setLoading(false);
    };

    const addFavorite = async () => {
        if (!selectedCrypto || !utilisateur) return;
        const newFavorite = getCryptoByIdFromList(selectedCrypto, cryptos);
        setFavorites([...favorites, newFavorite]);
        createFavori(utilisateur.id, selectedCrypto);
        setModalVisible(false);
        setSelectedCrypto(null); // Réinitialiser la sélection après ajout
        fetchFavorites(); // Rafraîchir les non-favorites
    };

    const removeFavorite = async (idCrypto) => {
        if (!utilisateur) return;

        const removedCrypto = favorites.find(fav => fav.idCrypto === idCrypto);

        const updatedFavorites = favorites.filter(fav => fav.idCrypto !== idCrypto);
        setFavorites(updatedFavorites);

        if (removedCrypto) {
            setNonFavorites([...nonFavorites, removedCrypto]);
        }

        await deleteFavori(utilisateur.id, idCrypto);
    };


    return (
        <View style={styles.container}>
            <Header title="Favoris"  />

            {loading && <ActivityIndicator size="large" color={theme.colors.primary} style={styles.loadingIndicator} />}

            <FlatList
                data={favorites}
                keyExtractor={(item) => item.idCrypto}
                ListEmptyComponent={<Text style={styles.emptyMessage}>Pas de Favoris Pour Le Moment</Text>}
                renderItem={({ item }) => (
                    <View style={styles.item}>
                        <Image source={getImagePath(item.nom)} style={styles.notificationLogo} />
                        <Text style={styles.nom}>{item.nom}</Text>
                        <TouchableOpacity onPress={() => removeFavorite(item.idCrypto)}>
                            <Icon name="heart" size={24} color="red" />
                        </TouchableOpacity>
                    </View>
                )}
            />

            <TouchableOpacity
                style={[styles.addButton, { bottom: height * 0.15 }]}
                onPress={() => setModalVisible(true)}>
                <Icon name="plus" size={30} color={theme.colors.text} />
            </TouchableOpacity>

            <Modal visible={modalVisible} animationType="slide" transparent>
                <View style={styles.modalContainer}>
                    <View style={styles.modalContent}>
                        <Text style={styles.modalTitle}>Ajouter un favori</Text>
                        <Picker
                            selectedValue={selectedCrypto}
                            onValueChange={(itemValue) => setSelectedCrypto(itemValue)}
                            style={styles.picker}>
                            <Picker.Item label="Sélectionnez Crypto" value={null} />
                            {nonFavorites.map(crypto => (
                                <Picker.Item key={crypto.idCrypto} label={crypto.nom} value={crypto.idCrypto} />
                            ))}
                        </Picker>
                        <View style={styles.buttonsContainer}>
                            <TouchableOpacity style={styles.modalButton} onPress={addFavorite}>
                                <Text style={styles.modalButtonText}>Ajouter</Text>
                            </TouchableOpacity>
                            <TouchableOpacity style={styles.modalButton} onPress={() => setModalVisible(false)}>
                                <Text style={styles.modalButtonText}>Annuler</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </View>
            </Modal>

            <NavBar activeScreen="Favoris" />
        </View>
    );
}


const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: theme.colors.background,
        padding: 20,
    },
    item: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: 15,
        backgroundColor: theme.colors.surface,
        borderRadius: 10,
        marginBottom: 10,
    },

    loadingIndicator: {
        marginBottom: 10,
        alignSelf: 'center',
    },

    nom: {
        color: theme.colors.text,
        fontSize: 18,
    },
    addButton: {
        position: 'absolute',
        right: 20,
        bottom: '15%',
        backgroundColor: theme.colors.primary,
        width: 60,
        height: 60,
        borderRadius: 30,
        justifyContent: 'center',
        alignItems: 'center',
        elevation: 5,
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
        alignItems: 'center',
    },
    modalTitle: {
        color: theme.colors.text,
        fontSize: 18,
        marginBottom: 15,
        textAlign: 'center',
    },
    picker: {
        width: '100%',
        height: 50,
        marginBottom: 20,
    },
    buttonsContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        width: '100%',
    },
    modalButton: {
        backgroundColor: theme.colors.primary,
        padding: 10,
        borderRadius: 5,
        flex: 1,
        marginHorizontal: 5,
        alignItems: 'center',
    },
    modalButtonText: {
        color: theme.colors.text,
        fontWeight: 'bold',
    },
    notificationLogo: {
        width: 40,
        height: 40,
        marginRight: 10,
    },
    valeur: {
        color: theme.colors.primary,
        fontSize: 16,
    },
    emptyMessage: {
        textAlign: 'center',
        fontSize: 18,
        marginTop: 20,
        color: theme.colors.primary,
    },
});