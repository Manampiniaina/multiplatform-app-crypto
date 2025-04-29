export const convertFirestoreTimestampToDate = (timestamp) => {
    if (!timestamp || !timestamp.seconds) return "";
    const date = new Date(timestamp.seconds * 1000 + timestamp.nanoseconds / 1e6);
    return date.toISOString().replace("T", " ").substring(0, 19);
  };

export const getCurrentDateTime = () => {
    const options = {
        weekday: 'long',
        day: 'numeric',
        month: 'long',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        timeZoneName: 'short'
    };

    return new Date().toLocaleString('fr-FR', options);
};
