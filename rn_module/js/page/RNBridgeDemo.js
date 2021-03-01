import React from 'react'
import {View, Text, Button, StyleSheet} from 'react-native'
import HiRNBridge from '../lib/HiRNBridge'
import HiImageView from '../lib/HiImageView'

export default class RNBridgeDemo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            header: '',
            message: ''
        }
    }

    async getHeader() {
        let header = await HiRNBridge.getHeaderParams();
        this.setState({
            header
        })
    }

    render(): React.ReactNode {
        const {header, message} = this.state;
        return <View>
            <Button title={'onBack'}
                    onPress={() => {
                        HiRNBridge.onBack(null);
                    }}
            />
            <Button title={'goToNative'}
                    onPress={() => {
                        HiRNBridge.goToNative({'goodsId': '1580374239317'})
                    }}
            />
            <Button title={'getHeaderParams'}
                    onPress={() => {
                        this.getHeader();
                    }}
            />
            <Text>获取的：{JSON.stringify(header)}</Text>
            {/*https://www.devio.org/img/beauty_camera/beauty_camera6.jpg*/}
            <HiImageView
                style={{width: 200, height: 200}}
                src={'https://www.devio.org/img/beauty_camera/beauty_camera1.jpg'}
                onPress={(e) => {
                    this.setState(
                        {
                            message: e
                        }
                    )
                }}
            />
            <Text>收到来在Native端的消息：{message}</Text>
        </View>;
    }

}
