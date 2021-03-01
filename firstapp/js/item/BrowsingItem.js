import React from 'react';
import {View, Text, StyleSheet, TouchableOpacity, Image} from 'react-native';

export default class BrowsingItem extends React.Component {
    getFixTags(tags = '') {
        const array = tags.split(' ');
        const result = [];
        for (let i = 0; i < array.length; i++) {
            if (i < 3) {
                result.push(array[i]);
            } else {
                break;
            }
        }
        return result;

    }

    render() {
        const {model: {goodsId, goodsName, createTime, tags, coverUrl, groupPrice, completedNumText} = {}} = this.props;
        const fixCoverUrl = coverUrl.startsWith('http') ? coverUrl : `https:${coverUrl}`;
        return <TouchableOpacity
            style={[styles.root, styles.row]}
            onPress={() => {
                this.props.onSelect && this.props.onSelect();
            }}
        >
            <Image
                style={styles.cover}
                source={{uri: fixCoverUrl}}
            />

            <View style={styles.content}>
                <View>
                    <Text style={styles.title}>{goodsName}</Text>
                    <View style={styles.row}>
                        {
                            this.getFixTags(tags).map((item, index) => {
                                return <Text key={index} style={styles.tag}>{item}</Text>;
                            })
                        }
                    </View>
                </View>
                <View>
                    <Text style={styles.completed}>{completedNumText}</Text>
                    <View style={styles.row}>
                        <Text style={styles.mark}>￥</Text>
                        <Text style={styles.price}>{groupPrice}</Text>
                    </View>
                    <View style={styles.line}/>
                </View>
            </View>
        </TouchableOpacity>;
    }

}
const styles = StyleSheet.create({
    root: {
        marginTop: 5,
        marginBottom: 5,
        marginLeft: 10,
        marginRight: 15,
    },
    row: {
        flexDirection: 'row',
    },
    cover: {
        height: 110,
        width: 110,
        borderRadius: 3,
    },
    content: {
        flex: 1,
        marginLeft: 10,
        justifyContent: 'space-between',
    },
    title: {
        color: '#151515',
        fontSize: 13,
    },
    tag: {
        color: '#f9590b',
        margin: 2,
        backgroundColor: '#fff2ea',
        paddingLeft: 3,
        paddingRight: 3,
        borderRadius: 3,
        fontSize: 10,
    },
    completed: {
        color: '#9c9c9c',
        fontSize: 10,
    },
    mark: {
        color: '#e02e22',
        fontSize: 8,
        marginTop: 6,
    },
    price: {
        color: '#e02e22',
        fontSize: 13,
    },
    line: {
        flex: 1,
        height: 0.8,
        marginTop: 10,
        backgroundColor: '#f3f3f3',
    },
});
