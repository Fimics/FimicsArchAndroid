import React from 'react';
import {View, StyleSheet, Text, FlatList, RefreshControl} from 'react-native';
import DataStore from '../lib/DataStore';
import NavigationBar from '../component/NavigationBar';
import BrowsingItem from '../item/BrowsingItem';

const PAGE_SIZE = 10;
const REFRESH_COLOR = '#d44949';
const URL = 'https://api.devio.org/as/browsing/histories?';
export default class BrowsingPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            list: [],
            isLoading: false,
        };
        this.pageIndex = 1;

    }

    componentDidMount() {
        this.loadData();
    }

    async loadData(loadMore) {
        if (loadMore) {
            this.pageIndex++;
        } else {
            this.pageIndex = 1;
        }
        const url = `${URL}pageIndex=${this.pageIndex}&pageSize=${PAGE_SIZE}`;

        try {
            const response = await new DataStore().fetchData(url);
            const {list, total} = response;
            if (list && list.length > 0) {
                this.setState(
                    {
                        list: loadMore ? this.state.list.concat(list) : list,
                        isLoading: false,
                    },
                );
            } else {
                if (loadMore) {
                    this.pageIndex--;
                }
                this.setState({
                    isLoading: false,
                });
            }
        } catch (e) {
            console.log(e);
            if (loadMore) {
                this.pageIndex--;
            }
            this.setState({
                isLoading: false,
            });
        }
    }

    renderItem(data) {
        const item = data.item;
        return <BrowsingItem
            model={item}
            onSelect={()=>{

            }}
        />;

    }

    render() {
        const {list, isLoading} = this.state;
        return <View style={styles.container}>
            <NavigationBar
                title={'历史浏览'}
                onBack={() => {

                }}
            />
            <FlatList
                data={list}
                renderItem={data => this.renderItem(data)}
                keyExtractor={item => '' + item.goodsId}
                refreshControl={
                    <RefreshControl
                        title={'Loading'}
                        titleColor={REFRESH_COLOR}
                        colors={[REFRESH_COLOR]}
                        refreshing={isLoading}
                        onRefresh={() => this.loadData()}
                        tintColor={REFRESH_COLOR}
                    />
                }
                onEndReached={() => {
                    console.log('----onEdnReached-----');
                    setTimeout(() => {//fix 滚动时两次调用onEndReached https://github.com/facebook/react-native/issues/14015
                        if (this.canLoadMore) {
                            this.loadData(true);
                            this.canLoadMore = false;
                        }
                    }, 100);
                }}
                onEndReachedThreshold={0.5}
                onMomentumScrollBegin={() => {
                    this.canLoadMore = true;//fix 初始化时页调用onEndReached的问题
                    console.log('----onMomentumScrollBegin-----');
                }}
            />
        </View>;
    }

}
const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
});
