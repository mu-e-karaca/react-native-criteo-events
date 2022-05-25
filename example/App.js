import React from 'react';
import type {Node} from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,Button
} from 'react-native';
import CriteoEvents from 'react-native-criteo-events';


export default class App extends React.Component {
  componentDidMount() {
    CriteoEvents.init("username","US","en","email");
    CriteoEvents.launch();
  }
  render(){
    return (
      <SafeAreaView >
        <StatusBar />
        <ScrollView
          contentInsetAdjustmentBehavior="automatic">
          <View style={styles.container}>
          <Text>CriteoEvents</Text>
          </View>
        </ScrollView>
      </SafeAreaView>
    );
  }
};

var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center"
  }
});
