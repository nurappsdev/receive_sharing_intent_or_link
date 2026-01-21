import 'package:flutter/material.dart';
import 'package:receive_sharing_intent/receive_sharing_intent.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _sharedText = "";

  @override
  void initState() {
    super.initState();

    // For sharing or opening URL
    ReceiveSharingIntent.instance.getMediaStream().listen((value) {
      if (value.isNotEmpty) {
        setState(() {
          // Extract text from the shared media
          _sharedText = value.map((media) => media.message ?? media.path).join('\n');
        });
      }
    }, onError: (err) {
      print("getMediaStream error: $err");
    });

    // If the app was terminated, then this will extract the shared data
    ReceiveSharingIntent.instance.getInitialMedia().then((value) {
      if (value.isNotEmpty) {
        setState(() {
          // Extract text from the initial shared media
          _sharedText = value.map((media) => media.message ?? media.path).join('\n');
        });
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: const Text("Receive Sharing Intent")),
        body: Center(
          child: Text("Shared text: $_sharedText"),
        ),
      ),
    );
  }
}