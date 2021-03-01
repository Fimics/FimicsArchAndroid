import React from 'react';
import {Text, View, TextInput, StyleSheet, ToastAndroid} from 'react-native';
import AsyncStorage from '@react-native-community/async-storage';

const KEY = 'AsyncStorageDemo';
export default class AsyncStorageDemo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: '',
            keys: [],
            values: [],
        };
        this.index = 1;
    }

    save() {
        AsyncStorage.setItem(KEY + this.index, this.valueText);
        this.index++;
    }

    async getValue() {
        let value = await AsyncStorage.getItem(KEY);
        this.setState({
            value: value,
        });
    }

    async getKeys() {
        let keys = await AsyncStorage.getAllKeys();
        this.setState(
            {
                keys: keys,
            },
        );
    }

    async getValues() {
        let values = await AsyncStorage.multiGet(this.state.keys);
        this.setState({
            values: values,
        });
    }

    remove() {
        AsyncStorage.removeItem(KEY);
    }

    showToast() {
        ToastAndroid.show('showToast', 2000);
    }

    render() {
        const {value, keys, values} = this.state;
        return <View>
            <View style={styles.row}>
                <TextInput
                    style={styles.input}
                    onChangeText={text => {
                        this.valueText = text;
                    }}
                />
                <Text
                    onPress={() => {
                        debugger;
                        this.save();
                    }}
                >保存</Text>
            </View>
            <View style={styles.row}>
                <Text
                    onPress={() => {
                        debugger;
                        this.getValue();
                    }}
                >获取</Text>
                <Text>{value}</Text>
            </View>
            <Text
                onPress={() => {
                    this.remove();
                }}
            >移除</Text>
            <Text
                onPress={() => {
                    this.getKeys();
                }}
            >getAllKeys</Text>
            <Text>keys:{keys}</Text>
            <Text
                onPress={() => {
                    this.getValues();
                }}
            >getValues</Text>
            <Text>values:{values}</Text>
            <Text onPress={() => {
                this.showToast();
            }}>Toast </Text>
        </View>;
    }

}
const styles = StyleSheet.create({
    input: {
        height: 40,
        borderWidth: 1,
        borderColor: 'gray',
        flex: 1,
        margin: 10,
    },
    row: {
        flexDirection: 'row',
    },
});

