import { useEffect, useState } from 'react';
import { Button, StyleSheet, Text, View } from 'react-native';

type Props = {
  source?: string;
};

export function HelloScreen({ source = 'unknown' }: Props) {
  const [count, setCount] = useState(0);

  useEffect(() => {
    console.log('[RN] HelloScreen mounted');
    return () => console.log('[RN] HelloScreen unmounted');
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Hello from React Native</Text>
      <Text>Opened from: {source}</Text>
      <Text style={styles.count}>Count: {count}</Text>
      <Button title="Increment" onPress={() => setCount(c => c + 1)} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    gap: 12,
    backgroundColor: '#E8F0FE',
  },
  title: {
    fontSize: 24,
    fontWeight: '600',
  },
  count: {
    fontSize: 20,
  },
});