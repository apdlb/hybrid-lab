import { useState } from 'react';
import { Pressable, StyleSheet, Text } from 'react-native';

type Props = {
  title?: string;
};

export function InlineCard({ title = 'RN card' }: Props) {
  const [likes, setLikes] = useState(0);

  return (
    <Pressable style={styles.card} onPress={() => setLikes(l => l + 1)}>
      <Text style={styles.title}>{title}</Text>
      <Text>Rendered by React Native · likes: {likes}</Text>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  card: {
    flex: 1,
    justifyContent: 'center',
    padding: 16,
    borderRadius: 12,
    backgroundColor: '#FFF4E5',
  },
  title: {
    fontSize: 18,
    fontWeight: '600',
  },
});