//
//  Main.swift
//  TaqwaWOD
//
//  Created by Wadood Chaudhary on 12/15/14.
//
//

import UIKit
class MainViewController:UIViewController {
    @IBOutlet weak var button:UIButton!
    
    @IBOutlet weak var rightSwitch: UISwitch!
    @IBOutlet weak var leftSwitch: UISwitch!
    @IBOutlet weak var txtFieldSearchBox: UITextField!
    @IBAction func toggleControls(sender: UISegmentedControl) {
    
    }
    @IBAction func switchChanged(sender: UISwitch) {
        let setting = sender.on
        rightSwitch.setOn(setting,animated:true)
        leftSwitch.setOn(setting,animated:true)
    }
    
    @IBAction func buttonPressed(sender: UIButton) {
        let controller = UIAlertController(title: "Are You Sure?",
            message:nil, preferredStyle: .ActionSheet)
        
        let yesAction = UIAlertAction(title: "Yes, I'm sure!",
            style: .Destructive, handler: { action in
                let msg = self.txtFieldSearchBox.text.isEmpty
                    ? "You can breathe easy, everything went OK."
                    : "You can breathe easy, \(self.txtFieldSearchBox.text),"
                    + " everything went OK."
                let controller2 = UIAlertController(
                    title:"Something Was Done",
                    message: msg, preferredStyle: .Alert)
                let cancelAction = UIAlertAction(title: "Phew!",
                    style: .Cancel, handler: nil)
                controller2.addAction(cancelAction)
                self.presentViewController(controller2, animated: true,
                    completion: nil)
        })
        
        let noAction = UIAlertAction(title: "No way!",
            style: .Cancel, handler: nil)
        
        controller.addAction(yesAction)
        controller.addAction(noAction)
        
        if let ppc = controller.popoverPresentationController {
            ppc.sourceView = sender
            ppc.sourceRect = CGRect(x: 0, y: 0, width: 100, height: 100)
        }
        
        presentViewController(controller, animated: true, completion: nil)
    }
    
    @IBAction func registerButtonPressed(sender: UIButton) {
        txtFieldSearchBox.resignFirstResponder()
        let title = sender.titleForState(.Normal)!
        let plainText = "\(title) button pressed"
        let styledText = NSMutableAttributedString(string: plainText)
        let attributes = [
            NSFontAttributeName:
                UIFont.boldSystemFontOfSize(12)
        ]
        let attributes2 = [
            NSFontAttributeName:
                UIFont.boldSystemFontOfSize(21)
        ]
        let secondString = plainText.substringFromIndex(advance(plainText.startIndex,5))
        let nameRange = (plainText as NSString).rangeOfString(title)
        let someRange = NSRange(location: 1,length: 5)
        //styledText.setAttributes(attributes, range: someRange)
        println("secondString:\(secondString)")
        //styledText.setAttributes(attributes2, range:NSRange(location: 6,length: 3))
        styledText.addAttribute(NSFontAttributeName, value: UIFont.boldSystemFontOfSize(10), range: NSRange(location: 1,length: 3))
        styledText.addAttribute(NSFontAttributeName, value: UIFont.boldSystemFontOfSize(10), range: NSRange(location: 6,length: 6))
        
        txtFieldSearchBox.attributedText = styledText
        //txtFieldSearchBox.text = plainText
        
        
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func textFieldDoneEditing(sender:UITextField) {
        sender.resignFirstResponder()
    }
    
    @IBAction func backgroundApp(sender:UIControl) {
        txtFieldSearchBox.resignFirstResponder()
    }
    
}
