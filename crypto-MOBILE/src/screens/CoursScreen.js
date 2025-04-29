import React from 'react';
import { View, Text, StyleSheet, ScrollView, Dimensions } from 'react-native';
import { LineChart } from 'react-native-chart-kit';
import Header from "../components/Header";
import theme from '../styles/theme';
import NavBar from "../components/NavBar";
import { useCryptos } from "../contexts/Context";

export default function CoursScreen() {
    const { cryptos } = useCryptos();
    const availableCryptos = cryptos.sort((a, b) => b.valeur - a.valeur);

    // Préparer les données du graphique
    const chartData = {
        labels: availableCryptos.map((_, index) => index.toString()), // X axis: index
        datasets: [{
            data: availableCryptos.map((crypto) => crypto.valeur), // Y axis: valeurs
        }],
    };

    return (
        <View style={styles.container}>
            <Header title="Cours Actuels " />

            {/* Graphique des cours */}
            <View style={styles.chartContainer}>
                <LineChart
                    data={chartData}
                    width={Dimensions.get("window").width - 40}
                    height={220}
                    bezier
                    onDataPointClick={({ value }) => {
                        alert(`Valeur : ${value}`);
                    }}
                    chartConfig={{
                        backgroundColor: theme.colors.surface,
                        backgroundGradientFrom: theme.colors.surface,
                        backgroundGradientTo: theme.colors.surface,
                        decimalPlaces: 2,
                        color: (opacity = 1) => `rgba(255, 255, 255, ${opacity})`,
                        labelColor: (opacity = 1) => `rgba(255, 255, 255, ${opacity})`,
                        style: { borderRadius: 10 },
                        propsForDots: {
                            r: "6",
                            strokeWidth: "2",
                            stroke: theme.colors.primary,
                        },
                    }}
                />

            </View>

            <ScrollView showsVerticalScrollIndicator={false} contentContainerStyle={styles.scrollViewContent}>
                {availableCryptos.map((item) => (
                    <View key={item.idCrypto} style={styles.item}>
                        <Text style={styles.id}>#</Text>
                        <Text style={styles.nom}>{item.nom}</Text>
                        <Text style={styles.valeur}>{item.valeur}</Text>
                    </View>
                ))}
            </ScrollView>

            <NavBar activeScreen="Cours" />
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: theme.colors.background,
        padding: 20,
    },
    scrollViewContent: {
        paddingBottom: 90,
    },
    chartContainer: {
        alignItems: 'center',
        marginBottom: 20,
        backgroundColor: theme.colors.surface,
        borderRadius: 10,
        padding: 10,
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
    id: {
        color: theme.colors.primary,
        fontSize: 16,
        marginRight: 10,
    },
    nom: {
        color: 'white',
        fontSize: 18,
        flex: 1,
    },
    valeur: {
        color: theme.colors.primary,
        fontSize: 16,
    },
});
